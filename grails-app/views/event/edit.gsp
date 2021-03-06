

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Event</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Event List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Event</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Event</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${event}">
            <div class="errors">
                <g:renderErrors bean="${event}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${event?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="artist">Artist:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:event,field:'artist','errors')}">
                                    <input type="text" id="artist" name="artist" value="${fieldValue(bean:event,field:'artist')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:event,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:event,field:'city')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country">Country:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:event,field:'country','errors')}">
                                    <input type="text" id="country" name="country" value="${fieldValue(bean:event,field:'country')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:event,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:event,field:'name')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startDate">Start Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:event,field:'startDate','errors')}">
                                    <g:datePicker name="startDate" value="${event?.startDate}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
