class DapperGigsServiceTests extends GroovyTestCase {

def dapperGigsService
def urlString="http://www.trinityconcerts.de/advanced_search_result.php/all/1"

    void setUp(){
      def base = "http://www.dapper.net/RunDapp?&&"
      def qs = []
        qs << "dappName=trinityconcertsde"
        qs << "v=1"
        qs << "applyToUrl="+urlString
      def url = new URL(base + qs.join("&"))
      def connection = url.openConnection()
      assertEquals "Connection with Dapper:trinity broken",connection.responseCode,200
    }


    void testgetEvents() {
      def list=dapperGigsService.getEvents()
      assert list.size()>0 : "Got no events from Dapper: trinity"

    }
}
