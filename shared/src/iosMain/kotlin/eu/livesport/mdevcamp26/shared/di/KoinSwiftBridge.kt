package eu.livesport.mdevcamp26.shared.di

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.ParametersHolder

@OptIn(BetaInteropApi::class)
object KoinSwiftBridge : KoinComponent {

    fun get(objCClass: ObjCClass, parameters: List<Any>?): Any {
        val kClazz = getOriginalKotlinClass(objCClass)
            ?: throw IllegalArgumentException("Could not find Kotlin class for $objCClass")

        return getKoin().get(
            clazz = kClazz,
            qualifier = null,
            parameters = parameters?.let { list -> { ParametersHolder(list.toMutableList()) } }
        )
    }
}
