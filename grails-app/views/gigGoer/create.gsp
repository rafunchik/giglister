  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create User</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
        </div>
        <div class="body">
            <h1>Create User</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${user}">
            <div class="errors">
                <g:renderErrors bean="${user}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class='prop'>
                                <td valign='top' class='name'>
                                    <label for='userName'>User Name:</label></td>
                                <td valign='top' class='value ${hasErrors(bean:user,field:'userName','errors')}'>
                                    <input type="text" id='userName' name='userName' value="${user?.userName?.encodeAsHTML()}"/></td>
                            </tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='firstName'>First Name:</label></td><td valign='top' class='value ${hasErrors(bean:user,field:'firstName','errors')}'><input type="text" id='firstName' name='firstName' value="${user?.firstName?.encodeAsHTML()}"/></td></tr>
                        
                            <tr class='prop'><td valign='top' class='name'><label for='lastName'>Last Name:</label></td><td valign='top' class='value ${hasErrors(bean:user,field:'lastName','errors')}'><input type="text" id='lastName' name='lastName' value="${user?.lastName?.encodeAsHTML()}"/></td></tr>

                            <tr class='prop'><td valign='top' class='name'><g:select name="city" id="city" from="${Event.cities}" value="${fieldValue(bean:event,field:'city')}" noSelection="['null':'Choose a city...']" /></td></tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create"></input></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
