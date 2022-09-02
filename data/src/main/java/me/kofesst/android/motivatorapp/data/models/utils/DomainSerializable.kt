package me.kofesst.android.motivatorapp.data.models.utils

/**
 * Интерфейс, представляющий конвертацию модели из
 * слоя domain в слой data.
 *
 * [Domain] - класс domain модели.
 */
interface DomainSerializable<Domain : Any> {
    /**
     * Конвертация data модели в модель [Domain].
     */
    fun toDomain(): Domain
}