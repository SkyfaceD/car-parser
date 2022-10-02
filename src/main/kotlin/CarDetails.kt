data class CarDetails(
    val price: String,
    val brand: String?,
    val name: String?,
    val year: String?,
    val city: String?,
    val body: String?,
    val engineVolume: String?,
    val mileage: String?,
    val transmission: String?,
    val steeringWheel: String?,
    val color: String?,
    val driveUnit: String?
) {
    companion object {
        val header: String
            get() = "price,brand,name,city,body,engineVolume,mileage,transmission,steeringWheel,color,driveUnit"
    }
}

fun CarDetails.asCsv(): String {
    return "$price,$brand,$name,$city,$body,$engineVolume,$mileage,$transmission,$steeringWheel,$color,$driveUnit"
}