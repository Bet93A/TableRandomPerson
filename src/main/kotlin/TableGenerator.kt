import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import net.datafaker.Faker
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

var faker = Faker(Locale.of("ru"))
val streets = mutableListOf("Баррикадная", "Бархотская", "Барьерная", "Баумана", "Летняя", "Летчиков", "Линейная",  "Лиственная",  "Минеральная",  "Минометчиков",  "Мира", "Новаторов", "Новая", "Прониной", "Просторная", "Профсоюзная", "Рябинина", "Рябиновая", "Саввы Белых", "Садовая", "Учителей", "Фабричная", "Шаумяна", "Буторина", "Бычковой", "Вайнера", "Войкова", "Вокзальная", "Волгоградская", "Гаршина", "Гастелло", "Генеральская", "Дзержинского", "Дивизионная", "Избирателей", "Изоплитная", "Ильича", "Индустрии", "Инженерная", "Ирбитская", "Иркутская")
val regions = mutableListOf("Амурская", "Архангельская", "Астраханская", "Белгородская", "Брянская", "Владимирская", "Волгоградская", "Вологодская", "Воронежская", "Ивановская", "Иркутская", "Калининградская", "Калужская", "Кемеровская", "Кировская", "Костромская", "Курганская", "Курская", "Ленинградская", "Липецкая", "Магаданская", "Московская", "Мурманская", "Нижегородская", "Новгородская", "Новосибирская", "Омская", "Оренбургская", "Орловская", "Пензенская", "Псковская", "Ростовская", "Рязанская", "Самарская", "Саратовская", "Сахалинская", "Свердловская", "Смоленская", "Тамбовская", "Тверская", "Томская", "Тульская", "Тюменская", "Ульяновская", "Челябинская", "Ярославская")
val pointColumnWidths = floatArrayOf(90f, 70f, 100f, 40f, 40f, 90f, 90f, 60f, 60f, 120f, 100f, 120f, 30f, 30f)
val table = Table(pointColumnWidths)

data class Fio(var name: String, var surname: String, var patronymic: String, var gender:String)
fun createFio():Fio{
    val fio = faker.name().nameWithMiddle()
    val name = fio.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    val patronymic = fio.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    val surname = fio.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
    fun Gender(): String {
        val result: String
        result = if ("ич" in patronymic)
            "МУЖ"
        else
            "ЖЕН"
        return (result)
    }
    val gender =Gender()
    return Fio(name,surname,patronymic,gender)
}

fun createDateBirthAndAge(): Pair<String,String> {
    val dateOfBirth = faker.date().birthday("dd-MM-yyyy")
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val nowDate = LocalDate.now().format(formatter)
    val startDate = LocalDate.parse(dateOfBirth, formatter)
    val endDate = LocalDate.parse(nowDate, formatter)
    val age = Period.between(startDate, endDate).getYears().toString()
    return Pair(dateOfBirth, age)
}

data class Address(var index:String, var country:String,var region:String, var city:String,var street:String,var home:String,var room:String)
fun createAddress():Address {
    val index = faker.address().postcode().toString()
    val country = "Россия"
    val city = faker.address().cityName()
    val street = streets[(0 until streets.size).random()]
    val region = regions[(0 until regions.size).random()]
    val home = (1..240).random().toString()
    val room = (1..500).random().toString()
    return Address(index,country,region,city,street,home,room)
}

class NameColumn() {
    fun addNameColumn() {
        table.addCell("Фамилия").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Имя").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Отчество").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Возраст").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Пол").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Дата рождения").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Место рождения").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Индекс").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Страна").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Область").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Город").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Улица").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Дом").setTextAlignment(TextAlignment.CENTER)
        table.addCell("Квартира").setTextAlignment(TextAlignment.CENTER)
    }
}

class DataTable(){
    fun addDataUser(){
        val(name,surname,patronymic, gender) =createFio()
        val(dateOfBirth,age) = createDateBirthAndAge()
        val(index,country,region,city,street,home,room) =createAddress()
        table.addCell(surname)
        table.addCell(name)
        table.addCell(patronymic)
        table.addCell(age)
        table.addCell(gender)
        table.addCell(dateOfBirth)
        table.addCell(city)
        table.addCell(index)
        table.addCell(country)
        table.addCell(region)
        table.addCell(city)
        table.addCell(street)
        table.addCell(home)
        table.addCell(room)
    }
}

fun main(args: Array<String>) {
    val dest = "/Users/betik/Documents/CreateTable.pdf" // путь сохранения таблицы
    val doc = Document(PdfDocument(PdfWriter(dest)), PageSize.A3.rotate())
    val fontFilename = "./src/main/resources/arial.ttf" // путь, где лежит шрифт
    val font: PdfFont = PdfFontFactory.createFont(fontFilename, PdfEncodings.IDENTITY_H)
    doc.setFont(font).setFontSize(10f)
    print("Сколько нужно сгенерировать данных(Введите число от 1 до 30): ")
    val n = try {
        readLine()!!.toInt()
    } catch (e: NumberFormatException) {
        println("Ввести можно только число!")
        return
    }
    if (n !in 1..30) {
        println("Нужно вводить только число в диапазоне от 1 до 30!")
        return
    }
    val nameColumn = NameColumn()
        nameColumn.addNameColumn()
        var i = 1
        for (i in 1..n as Int) run {
            val dataTable = DataTable()
            dataTable.addDataUser()
            val i = i + 1
        }
    doc.add(table)
    doc.close()
    println("Файл создан. Путь: $dest")
}