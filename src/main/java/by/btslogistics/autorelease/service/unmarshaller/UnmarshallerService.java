package by.btslogistics.autorelease.service.unmarshaller;

/**
 *
 *  работа с xml
 */

public interface UnmarshallerService {

    /**
     *
     *   Парсинг xml
     *
     * @param xml - xml в виде строки
     * @param customClass - тип обьекта (пользовательский класс), в который парсится xml
     * @return - возвращается объект типа SdMainDto
     */
    <T> T unmarshal(String xml, Class customClass);


    /**
     *  Проверка на xsd
     *
     * @param xml - xml в виде строки
     * @param customClass -  тип обьекта для xml
     * @param schemaLocation - расположение схемы xsd в каталоге XSD для проверки на соответствие/
     * @return - возвращается объект
     */
    <T> T unmarshalSchemaAll(String xml, Class customClass, String schemaLocation);

    /**
     * Преобразование объекта в XML строку
     *
     * @param object      Объект для преобразования
     * @param customClass Класс объекта для преобразования
     * @return XML документ в виде строки
     */
    String marshal(Object object, Class<?> customClass);
}
