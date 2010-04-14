
import org.springframework.context.*
import org.codehaus.groovy.grails.commons.ApplicationHolder

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import java.util.Calendar
import org.quartz.*
import groovy.sql.Sql

class EventUpdaterJobTests extends GroovyTestCase implements ApplicationContextAware{
    def  quartzScheduler

    ApplicationContext appContext
    Trigger  trigger
                            
    EventUpdaterJob  job
          def dataSource

    void setUp(){
      trigger=quartzScheduler.getTriggersOfJob("EventUpdaterJob","events")[0]
      println "Trigger: "+trigger
      assertTrue "no EventUpdaterJob bean fouhd", appContext.containsBean("EventUpdaterJob")
      job=appContext.getBean("EventUpdaterJob")

    }


   void testCronSchedule() {
        Calendar c=Calendar.instance
        c.set(Calendar.HOUR_OF_DAY,4)
        def expectedDate = trigger.getNextFireTime()
       println "date "+expectedDate 
        Calendar exp=Calendar.instance
        exp.setTime(expectedDate)
        assertEquals "trigger hour not as expected",c.HOUR_OF_DAY, exp.HOUR_OF_DAY
    }



    def void setApplicationContext(ApplicationContext ctx) throws BeansException {
        appContext = ctx;
    }

    void testUpdating() {
        assertEquals "table initially not empty",findTableCount("event",null),0
        job.execute()
        assertNotSame "no events retrieved",findTableCount("event",null),0
    }

    protected int findTableCount(String table, String where = null)  {

        String sqlSelect = "SELECT COUNT(0) FROM " + table
        if (where) {
          sqlSelect += " WHERE " + where
        }

        Sql.newInstance(dataSource).firstRow(sqlSelect)[0]
    }

}
    