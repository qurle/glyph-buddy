package net.qurle.glyphs.toys.buddy

import android.content.Context
import android.util.Log
import com.nothing.ketchum.GlyphMatrixManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.qurle.glyphs.toys.GlyphMatrixService
import net.qurle.glyphs.toys.buddy.utils.getBatteryPercentage
import net.qurle.glyphs.toys.utils.show
import kotlin.random.Random

class BuddyService : GlyphMatrixService("Glyph-Buddy") {

    lateinit var storedContext: Context
    lateinit var gmm: GlyphMatrixManager

    // Coroutine scope tied to the service's lifecycle
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val bgScope = CoroutineScope(Dispatchers.IO)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var periodicBatteryCheckJob: Job? = null
    private var randomSideEyeJob: Job? = null

    companion object {
        private const val BATTERY_CHECK_INTERVAL_MS = 5 * 60 * 1000L // 5 minutes
        private const val BASE_SIDE_EYE_INTERVAL_MS = 1 * 60 * 1000L
        private const val SIDE_EYE_VARIATION_PERCENTAGE = 0.05
        private const val SIDE_EYE_DURATION_MS = 3500L
    }


    override fun performOnServiceConnected(
        context: Context,
        glyphMatrixManager: GlyphMatrixManager
    ) {
        storedContext = context
        gmm = glyphMatrixManager

        val face = BuddyFace(context)
//        show(face.default(), storedContext, gmm)

        // Start periodic checks
        updateFaceBasedOnBattery(face)
        startPeriodicBatteryChecks(face)
        startRandomSideEyeBehavior(face)

    }

    override fun onTouchPointLongPress() {
        glyphMatrixManager?.let { gmm ->
            bgScope.launch {
                val face = BuddyFace(storedContext)
                show(face.wink(), storedContext, gmm)
                delay(500L)
                show(face.default(), storedContext, gmm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BuddyService", "Service Destroyed")
        stopPeriodicBatteryChecks()
        serviceScope.cancel()
    }


    private fun startPeriodicBatteryChecks(face: BuddyFace) {
        if (periodicBatteryCheckJob?.isActive == true) return
        periodicBatteryCheckJob = serviceScope.launch {
            while (isActive) {
                delay(BATTERY_CHECK_INTERVAL_MS)
                updateFaceBasedOnBattery(face)
            }
        }
    }

    private fun stopPeriodicBatteryChecks() {
        periodicBatteryCheckJob?.cancel()
        periodicBatteryCheckJob = null
    }

    private fun updateFaceBasedOnBattery(face: BuddyFace) {
        val batteryPercent = getBatteryPercentage(storedContext)

        val tiredness = when {
            batteryPercent <= 3 -> 3
            batteryPercent <= 10 -> 2
            batteryPercent <= 20 -> 1
            else -> 0
        }
        show(face.tired(tiredness), storedContext, gmm)
    }

    private fun getRandomizedSideEyeDelayMs(): Long {
        val variationAmount = BASE_SIDE_EYE_INTERVAL_MS * SIDE_EYE_VARIATION_PERCENTAGE
        val randomVariation = Random.nextDouble(-variationAmount, variationAmount)
        return (BASE_SIDE_EYE_INTERVAL_MS + randomVariation).toLong().coerceAtLeast(1000L)
    }

    private fun startRandomSideEyeBehavior(face: BuddyFace) {
        if (randomSideEyeJob?.isActive == true) return
        randomSideEyeJob = serviceScope.launch {
            while (isActive) {
                val currentGlyphManager = this@BuddyService.glyphMatrixManager ?: break
                delay(getRandomizedSideEyeDelayMs())
                if (!isActive) break

                show(face.sideEye(), storedContext, currentGlyphManager)
                delay(SIDE_EYE_DURATION_MS)
                if (!isActive) break

                updateFaceBasedOnBattery(face) // Reverts to battery-aware face
            }
        }
    }
}