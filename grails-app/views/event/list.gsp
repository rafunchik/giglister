

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Event List</title>
    </head>
    <body>
        <div class="nav">
              <span class="menuButton"><g:link class="create" action="create">New Event</g:link></span>
        </div>
        <div class="body">
            <h1>Event List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <%-- <g:sortableColumn property="id" title="Id" /> --%>
                        
                   	        <g:sortableColumn property="Artist" title="Artist" />
                        
                   	        <g:sortableColumn property="name" title="Name" />

                   	        <g:sortableColumn property="description" title="Price and Description" />
                        
                   	        <g:sortableColumn property="place" title="Place" />

                	        <g:sortableColumn property="startDate" title="Start Date" />

                            <g:sortableColumn property="startTime" title="Start Time" />

                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${events}" status="i" var="event">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <%-- <td><g:link action="show" id="${event.id}">${event.id?.encodeAsHTML()}</g:link></td> --%>
                        
                            <td>${event.artist?.encodeAsHTML()}</td>
                        
                            <td>${event.name?.encodeAsHTML()}</td>
                        
                            <td>${event.description}</td>
                        
                            <td>${event.place?.encodeAsHTML()}</td>
                        
                            <td><g:formatDate format="  E,d,MMM,yyyy  " date="${event.startDate}"/></td>

                            <td>${event.startTime?.encodeAsHTML()}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
               <g:paginate total="${totalEvents}" /> 
            </div>
        </div>
    </body>
</html>
