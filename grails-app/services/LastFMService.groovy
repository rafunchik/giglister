class LastFMService {

  boolean transactional = true
  final def MAX_PAGES = 100

  def base = "http://ws.audioscrobbler.com/2.0/?"
  // http://ws.audioscrobbler.com/2.0/?method=geo.getevents&country=spain&api_key=f8fd68b1cf891056c71422b3043c1208...


  def getEventsByCity(String city,int page) {
      def qs = []
        qs << "method=geo.getevents"
        qs << "location=" + URLEncoder.encode(city)
        qs << "api_key=f8fd68b1cf891056c71422b3043c1208"
        qs<< "page=" + page
      def url = new URL(base + qs.join("&"))
      return getEvents(url)
    }

  def getEventsByCity(String city){
        for (int i=0;i<Math.min(getTotalPagesByCity(city),MAX_PAGES);i++){
               getEventsByCity(city,i+1)
        }
    }

  def getTotalPagesByCity(String city){
        def qs = []
        qs << "method=geo.getevents"
        qs << "location=" + URLEncoder.encode(city)
        qs << "api_key=f8fd68b1cf891056c71422b3043c1208"
        def url = new URL(base + qs.join("&"))
        def connection = url.openConnection()
        if(connection.responseCode == 200){
            def xml = connection.content.text
            def lfm = new XmlSlurper().parseText(xml)
            def total = Integer.parseInt(lfm.events.@totalpages.text())
            return total
        }
        else{
            log.error("last fm service FAILED")
            log.error(url)
            log.error(connection.responseCode)
            log.error(connection.responseMessage)
        }
    }
  def getEventsByArtist(String artist){
      for (int i=0;i<Math.min(lastFMService.getTotalPagesByCity(city),MAX_PAGES);i++){
               lastFMService.getEventsByCity(city,i+1)
        }
    }
    
    def getEventsByArtist(String artist,int page) {
      def qs = []
      qs << "method=artist.getevents"
      qs << "artist=" + URLEncoder.encode(artist)
      qs << "api_key=f8fd68b1cf891056c71422b3043c1208"
      qs<< "page=" + page
      def url = new URL(base + qs.join("&"))
      return getEvents(url)
    }

    def getEvents(URL url){
      def connection = url.openConnection()
      def results
      if(connection.responseCode == 200){
        def xml = connection.content.text
        def lfm = new XmlSlurper().parseText(xml)
        results = lfm.events.event
        results.each {
            /*def artists =new String()
            it.artists.artist.each{
               artists<<it.artist.text()
            } */
            def place=it.venue.location.street.text()+", "+it.venue.location.postalcode.text()
    
            


            def df2 = new java.text.SimpleDateFormat("EEE, dd MMM yyyy")
            def date = df2.parse(it.startDate.text())
            def time =it.startTime.text()
            if(time){
                Calendar c=Calendar.getInstance()
                c.setTime(date)
                try{
                    c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time.split(":")[0]))
                    c.set(Calendar.MINUTE,Integer.parseInt(time.split(":")[1]))
                    date=c.getTime()
                }
                catch(NumberFormatException e){
                    println "Wrong time"
                }
            }


            def artist=it.artists.headliner.text()
            if(DapperGigsService.checkSaved(artist,date)){
                    //event already saved
                    log.info("Event already saved")
            }
            else{
                //def description=it.description.text().replaceAll("<img src(.)+/>", "<br />").replaceAll("<object(.)+/object>", "<br />")
                def description=it.description.text().replaceAll("<(.)+>","html")
                def event=new Event(artist:artist,country:it.venue.location.country.text(),description:"description",city:it.venue.location.city.text(),name:it.title.text(),place:place,startDate:date,startTime:time)
                //added from giglister-copy
                //def event=new Event(artist:artist,name:it.title.text(),description:description,city:it.venue.location.city.text(),place:place,startDate:date,startTime:time)

                if( !event.save(flush:true) ) {
                   event.errors.each {
                        println it
                        log.error(it)
                   }
                }
                

            }

        }
      }
      else{
        log.error("last fm service FAILED")
        log.error(url)
        log.error(connection.responseCode)
        log.error(connection.responseMessage)
      }
      return results

    }
}

