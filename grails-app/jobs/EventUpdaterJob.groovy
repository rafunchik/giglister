class EventUpdaterJob {
    def group = "events"
    def name = "EventUpdaterJob" 
    def dapperGigsService
    def lastFMService

    static triggers = {
        cronTrigger startDelay:100000, cronExpression: '0 0 5 * * ?'
    }

    def execute() {
        dapperGigsService.getEvents()
        Event.cities.each
        {
            for (int i=0;i<Math.min(lastFMService.getTotalPagesByCity(it),lastFMService.MAX_PAGES);i++){
                   lastFMService.getEventsByCity(it,i+1)
            }
        }
        log.println("Event updater ran at: "+new Date())
    }
}