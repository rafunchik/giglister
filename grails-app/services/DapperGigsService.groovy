/**
 * Created by IntelliJ IDEA.
 * User: rcastro
 * Date: 05-Jan-2009
 * Time: 18:09:52
 * To change this template use File | Settings | File Templates.
 */
import java.text.ParseException

public class DapperGigsService {
   def urlString="http://www.trinityconcerts.de/advanced_search_result.php/all/1"
   boolean transactional = true

   def getEvents(String city){
     if(city.equalsIgnoreCase("Berlin"))
         return getEvents() 
   }

   def getEvents() {
      def base = "http://www.dapper.net/RunDapp?&&"
      def qs = []
        qs << "dappName=trinityconcertsde"
        qs << "v=1"
        qs << "applyToUrl="+urlString
      def url = new URL(base + qs.join("&"))
      def connection = url.openConnection()
       

      def results
      if(connection.responseCode == 200){
        def xml = connection.content.text
        def lfm = new XmlSlurper().parseText(xml)
        results = lfm.Gig
        results.each {
            def artist=it.Artist.text()
            def event=new Event(artist:artist,name:artist,city:"Berlin",description:it.Price.text())
            String[]ar=it.Time_and_place.text().split(",")
            Calendar c=Calendar.getInstance()
            if(ar[0]){
                def locale = Locale.GERMAN
                def df2 = new java.text.SimpleDateFormat("dd. MMMMM yyyy",locale)
                try{
                    def date = df2.parse(ar[1])
                    c.setTime(date)
                    c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(ar[3].substring(1,3).trim()))
                    if(ar[3].split(":").length>1)
                    c.set(Calendar.MINUTE,Integer.parseInt(ar[3].split(":")[1]))
                    event.startDate=c.getTime()
                }
                catch(ParseException e){
                    println "Wrong date format "+e.getMessage()
                    log.info("Wrong date format "+e.getMessage())                    
                }
                catch(NumberFormatException e){
                    println "Wrong time"
                    log.info("Wrong time")
                }

                event.place=ar[2]
                event.startTime=ar[3]
            }

            if (event.startDate){
                if(checkSaved(artist,event.startDate)){
                    //event already saved
                    log.info("Event already saved")
                }
                else{
                    if( !event.save(flush:true) ) {
                           event.errors.each {
                                println it
                             }
                        }
                }

            }
        }
      }
      else{
        log.error("dapper service FAILED")
        log.error(url)
        log.error(connection.responseCode)
        log.error(connection.responseMessage)
      }
      return results
    }


    static def checkSaved(String artist,Date date){
                Calendar c=Calendar.getInstance()
                c.setTime(date)
                c.set(Calendar.HOUR_OF_DAY,0)
                c.set(Calendar.MINUTE,0)
                def initialDate=new Date(c.getTime().time-1)
                def topDate=c.getTime()+1
                def art='%'+artist+'%'
                return Event.findByArtistIlikeAndStartDateBetween(art,initialDate,topDate)
    }




}