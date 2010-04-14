  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit User</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="home" controller="user" action="show" id="${session.user.id}">Home</g:link></span>
            <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
        </div>
        <div class="body">
            <h1>Edit User</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${user}">
            <div class="errors">
                <g:renderErrors bean="${user}" as="list" />
            </div>
            </g:hasErrors>
            <g:form controller="user" method="post" >
                <input type="hidden" name="userName" value="${user?.userName}" />
                <div class="dialog">
                    <table>
                        <tbody>
                             <tr class='prop'><td valign='top' class='name'><label for='favArtists'>Fav Artists:</label></td><td valign='top' class='value ${hasErrors(bean:user,field:'favArtists','errors')}'>
                        <!--<tr>-->
                          <ul>
                            <g:each var='c' in='${user.refresh().favArtists}'>
                              <li>${c}</li>
                            </g:each>
                          </ul>
                            <g:form>
                            <g:textField name="fav" value="${fav}" />
                              <g:actionSubmit value="AddArtist"/>
                            </g:form>
                            <!--</tr>-->
                                %{--<g:link controller='user' params='["userName":user.userName]' action='addArtist'>Add Artist</g:link>--}%
                           </td></tr>
                        
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
