import org.springframework.context.*
import org.codehaus.groovy.grails.commons.ApplicationHolder

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import groovy.sql.Sql

import java.util.Calendar
import org.quartz.*
import EventDeleteJob;


class EventDeleteJobTests extends GroovyTestCase implements ApplicationContextAware{
    ApplicationContext appContext
    EventDeleteJob  job
    def dataSource
    Trigger  trigger
    def  quartzScheduler

    void setUp(){
     trigger=quartzScheduler.getTriggersOfJob("EventDeleteJob","events")[0]
     println "Trigger: "+trigger
     assertTrue "no EventDeleteJob bean fouhd", appContext.containsBean("EventDeleteJob")
     job=appContext.getBean("EventDeleteJob")
    }

    void testDeleting() {
        def date=new Date()-15
        def event =new Event(startDate:date)
        event.save(flush:true)
        job.execute()
        assertEquals "table not empty",findTableCount("event",null),0
    }


    void testCronSchedule() {
        Calendar c=Calendar.instance
        c.set(Calendar.HOUR_OF_DAY,5)
        c.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
        def expectedDate = trigger.getNextFireTime()
        println "date "+expectedDate
        Calendar exp=Calendar.instance
        exp.setTime(expectedDate)
        assertEquals "trigger hour not as expected",c.HOUR_OF_DAY, exp.HOUR_OF_DAY
        assertEquals "trigger date not as expected",c.DAY_OF_WEEK, exp.DAY_OF_WEEK
    }

    protected int findTableCount(String table, String where = null)  {

        String sqlSelect = "SELECT COUNT(0) FROM " + table
        if (where) {
          sqlSelect += " WHERE " + where
        }

        Sql.newInstance(dataSource).firstRow(sqlSelect)[0]
    }

        def void setApplicationContext(ApplicationContext ctx) throws BeansException {
        appContext = ctx;
    }

}
