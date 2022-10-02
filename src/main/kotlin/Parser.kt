import org.jsoup.Jsoup

class Parser  {
    fun ids(html: String): List<String> {
        val ids = arrayListOf<String>()

        val doc = Jsoup.parse(html)
        val list = doc.select("#results").select("div.a-list")[0]
        val cards = list.getElementsByClass("a-card")
        cards.forEach {
            ids.add(it.attr("data-id"))
        }

        return ids
    }

    fun details(html: String): CarDetails {
        val doc = Jsoup.parse(html)

        val titleSpans = doc.select("h1.offer__title").select("span")
        val titleHashMap = hashMapOf<String?, String>()
        for (span in titleSpans) {
            if (span.className() == "year") {
                titleHashMap["year"] = span.text().trim()
                break
            }
            titleHashMap[span.attr("itemprop")] = span.text().trim()
        }

        val sidebar = doc.select("div.offer__sidebar-info").first()

        val price = sidebar.select("div.offer__price").text().trim().replace("₸", "").replace(" ", "")

        val params = sidebar.select("dl")
        val paramsHashMap: HashMap<String?, String> = hashMapOf()
        for (param in params) {
            paramsHashMap[param.select("dt").attr("title")] = param.select("dd").text().trim()
        }

        return CarDetails(
            price = price,
            brand = titleHashMap["brand"] ?: "NaN",
            name = titleHashMap["name"] ?: "NaN",
            year = titleHashMap["year"] ?: "NaN",
            city = paramsHashMap["Город"] ?: "NaN",
            body = paramsHashMap["Кузов"] ?: "NaN",
            engineVolume = paramsHashMap["Объем двигателя, л"] ?: "NaN",
            mileage = paramsHashMap["Пробег"] ?: "NaN",
            transmission = paramsHashMap["Коробка передач"] ?: "NaN",
            steeringWheel = paramsHashMap["Руль"] ?: "NaN",
            color = paramsHashMap["Цвет"] ?: "NaN",
            driveUnit = paramsHashMap["Привод"] ?: "NaN"
        )
    }
}