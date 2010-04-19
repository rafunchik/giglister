class LastFMServiceTests extends GroovyTestCase {
def lastFMService
def url

    void setUp(){
      def map=[method:"geo.getevents",location:"Berlin",api_key:"f8fd68b1cf891056c71422b3043c1208"]
      def base = "http://ws.audioscrobbler.com/2.0/?"
      def qs = []
      map.each{
          qs<<it.key+"="+it.value
      }
      url = new URL(base + qs.join("&"))
      def connection = url.openConnection()
      assertEquals "Connection with LastFm broken",connection.responseCode,200
    }

    void testgetEventsByCity() {
      def list=lastFMService.getEvents(url)
      assert list.size()>0 : "Got no events from LAstFM"

    }


}
