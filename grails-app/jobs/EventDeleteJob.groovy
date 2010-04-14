import groovy.sql.Sql

import org.apache.log4j.Logger


class EventDeleteJob {
    def group = "events"
    def name = "EventDeleteJob" 
    static triggers = {
        cronTrigger startDelay:100000, cronExpression: '0 0 5 ? * WED'

    }

    def execute() {
        Date date=new Date()-7;
        Event.findAll("from Event as e where e.startDate<?",[date]).each{
            it.delete()
        }
        //Event.findByStartDateBefore(date).each{
          //  it.delete()
        //}
        log.println("Event delete ran")
    }


}
