package net.qurle.glyphs.toys.buddy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.nothing.ketchum.GlyphMatrixManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import net.qurle.glyphs.toys.GlyphMatrixService
import net.qurle.glyphs.toys.buddy.utils.createBatteryReceiver

class Service : GlyphMatrixService("Glyph-Buddy") {

    private lateinit var context: Context
    private lateinit var gmm: GlyphMatrixManager
    private lateinit var eventHandler: EventHandler
    private lateinit var batteryReceiver: BroadcastReceiver

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun performOnServiceConnected(
        context: Context,
        glyphMatrixManager: GlyphMatrixManager
    ) {
        this.context = context
        this.gmm = glyphMatrixManager
        eventHandler = EventHandler(scope, context, gmm)
        eventHandler.startEventLoop()
        batteryReceiver = createBatteryReceiver { state ->
            if (!isInitialized()) return@createBatteryReceiver
            eventHandler.handleStateChange(state)
        }
        context.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onTouchPointLongPress() {
        if (!isInitialized()) return
//        eventHandler.handleStateChange(State.WINK)
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(batteryReceiver)
        scope.cancel()
    }

    private fun isInitialized(): Boolean {
        return ::context.isInitialized && ::gmm.isInitialized && ::eventHandler.isInitialized
    }
}
