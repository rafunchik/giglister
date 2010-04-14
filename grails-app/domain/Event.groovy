

class Event {
    String artist
    String name
    String city
    Date startDate
    String place
    String startTime
    String description
    //int lastfmId
    //
    // artists []
    String country
    static def cities=["Berlin","London","Madrid","Moscow"]

   static constraints = {
        city(nullable:true, blank:true)
        startDate(nullable:false)
        place(nullable:true, blank:true)
        startTime(nullable:true, blank:true)
        country(nullable:true, blank:true)
        description(nullable:true, blank:true) 
   }


   static mapping = {
    sort "startDate":"desc"
   }


}
