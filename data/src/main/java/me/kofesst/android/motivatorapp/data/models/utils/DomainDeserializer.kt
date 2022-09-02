package me.kofesst.android.motivatorapp.data.models.utils

/**
 * Интерфейс, представляющий конвертацию модели из
 * слоя domain в слой data.
 *
 * [Domain] - класс domain модели.
 *
 * [Data] - класс data модели.
 */
interface DomainDeserializer<Domain : Any, Data : Any> {
    /**
     * Конвертация [domain] модели в модель [Data].
     */
    fun fromDomain(domain: Domain): Data
}