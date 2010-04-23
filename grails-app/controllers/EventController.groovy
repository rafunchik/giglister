import grails.converters.*
import org.apache.commons.lang.StringUtils

class EventController {
     def lastFMService
     def dapperGigsService
     def defaultAction = 'list'
     def sessionFactory

//    def geocode = {
//      //def result = lastFMService.getEvents(params.artist)
//      def result = dapperGigsService.getEvents()
//      render resuconfig.grlt as JSON
//    }

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!servletContext["gotEvents"])
            this.events()
        if(!params.max) params.max = 20
        if (!params.offset) params.offset = 0
        Calendar c=Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY,0)
        c.set(Calendar.MINUTE,0)
        def totalResults=Event.countByStartDateGreaterThanEquals(c.getTime())
        return [ "events": Event.findAllByStartDateGreaterThanEquals(c.getTime(),[max:params.max,offset:params.offset,sort:"startDate", order:"asc"]), "totalEvents":totalResults]
 
    }

    
    private void events() {
        dapperGigsService.getEvents()
        Event.cities.each
        {
            for (int i=0;i<Math.min(lastFMService.getTotalPagesByCity(it),10);i++){
                   lastFMService.getEventsByCity(it,i+1)
            }
        }
    }


    def show = {
        def event = Event.get( params.id )

        if(!event) {
            flash.message = "Event not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ event : event ] }
    }

    def delete = {
        def event = Event.get( params.id )
        if(event) {
            event.delete()
            flash.message = "Event ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Event not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def event = Event.get( params.id )

        if(!event) {
            flash.message = "Event not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ event : event ]
        }
    }

    def update = {
        def event = Event.get( params.id )
        if(event) {
            event.properties = params
            if(!event.hasErrors() && event.save()) {
                flash.message = "Event ${params.id} updated"
                redirect(action:show,id:event.id)
            }
            else {
                render(view:'edit',model:[event:event])
            }
        }
        else {
            flash.message = "Event not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def event = new Event()
        event.properties = params
        return ['event':event]
    }

    def save = {
          def event = new Event(params)
          if(!event.hasErrors() && event.save()) {
            flash.message = "Event ${event.id} created"
            redirect(action:show,id:event.id)
          }
          else {
            render(view:'create',model:[event:event])
          }
    }

    def render = {
        render(contentType: 'text/html;charset=UTF-8', view: 'list')
    }

    def getMyEvents ={
      //test
      //deleteEvents()
      if(Event.count()<10)
        this.events()
      if(!params.max) params.max = 20
      if (!params.offset)
        params.offset = 0
      def events=[]
      def city=params.city
      def user=session.user
      if (user)
      {
         def hibSession=sessionFactory.getCurrentSession()
         hibSession.refresh(user)
         city=user.city
         for (artist in user.favArtists){
            //change for case insensitive Criteria
            events.addAll(getEventsByCityAndArtist(artist, city, params))
            //[max:params.max,offset:params.offset,sort:"startDate", order:"asc"]
              //render(view:list,var:[events:events,totalEvents:12])
         }
      }
      else
      {
         def artist=params.artist
         if(StringUtils.isNotBlank(city) && StringUtils.isNotBlank(artist))
         {
            events.addAll(getEventsByCityAndArtist(artist, city, params))
         }
      }
      return [ "events": events,"totalEvents":events.size()]
    }

  private List getEventsByCityAndArtist(artist, city, Map params) {
    return Event.findAllByArtistIlikeAndCity('%' + artist + '%', city, [max: params.max, offset: params.offset, sort: "startDate", order: "asc"])
  }

  private void deleteEvents()
    {
        Date date=new Date()-7;
        Event.list().each{
            it.delete(flush:true)
        }
    }

}
