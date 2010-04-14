class EventUpdaterJob {
    def group = "events"
    def name = "EventUpdaterJob" 
    def dapperGigsService
    def lastFMService

    static triggers = {
        cronTrigger startDelay:100000, cronExpression: '0 0 4 * * ?'
    }

    def execute() {
        dapperGigsService.getEvents()
        Event.cities.each
        {
            for (int i=0;i<Math.min(lastFMService.getTotalPagesByCity(it),10);i++){
                   lastFMService.getEventsByCity(it,i+1)
            }
        }
        log.println("Event updater ran")
    }
}