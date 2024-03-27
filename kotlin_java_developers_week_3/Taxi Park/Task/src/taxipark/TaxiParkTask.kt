package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val driversWithTrips = this.trips.map { it.driver }.toSet()
    return this.allDrivers.minus(driversWithTrips)
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    if (minTrips == 0) {
        return this.allPassengers
    }
    return this.trips.repeatedPassengers(minTrips)
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val currentDriverTrips = this.trips.filter { driver == it.driver }
    return currentDriverTrips.repeatedPassengers(2)
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (withDiscount, withoutDiscount) =
        this.trips.partition {
            val discount = it.discount
            if (discount == null) {
                false
            } else {
                discount > 0
            }
        }
    val discountByPassenger = withDiscount.groupByPassenger()
    val withoutDiscountByPassenger = withoutDiscount.groupByPassenger()

    return discountByPassenger.filter {
        val amountWithoutDiscount =
            withoutDiscountByPassenger
                .getOrDefault(it.key, 0)

        it.value > amountWithoutDiscount
    }.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
	if (this.trips.isEmpty()) {
		return null
	}
    val groups =
        this.trips.groupingBy {
            ((it.duration / 10) * 10)..(((it.duration / 10) * 10) + 9)
        }.eachCount()
    return groups.maxBy { it.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) {
        return false
    }
    val totalIncome = this.trips.sumOf { it.cost }

    val top20Income =
        this.trips
            .groupBy { it.driver }
            .map { (_, tripsByDriver) -> tripsByDriver.sumOf { it.cost } }
            .sortedByDescending { it }
            .take(Math.floor(this.allDrivers.size * 0.2).toInt())
            .sumOf { it }

    return top20Income >= (totalIncome * 0.8)
}

fun List<Trip>.groupByPassenger(): Map<Passenger, Int> {
    return this.map {
        it.passengers
    }
        .flatten()
        .groupingBy { it }
        .eachCount()
}

fun List<Trip>.repeatedPassengers(minTrips: Int): Set<Passenger> {
    val groupedPassengers =
        this.groupByPassenger().filter {
            it.value >= minTrips
        }
    return groupedPassengers.keys
}
