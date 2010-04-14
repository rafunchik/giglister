

class UserController {
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max)params.max = 10
        [ userList: User.list( params ) ]
    }

    def show = {
        [ user : User.get( params.id ) ]
    }

    def delete = {

//        if (session.user?.id != params.id) {
//    		flash.message = "You can only delete yourself"
//    		redirect(action:list)
//    		return
//        }

        def user = User.get( params.id )

        if(user) {
            user.delete()
            flash.message = "User ${params.id} deleted."
            redirect(action:list)
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
//        if (session.user?.id != params.id) {
//    		flash.message = "You can only edit yourself"
//    		redirect(action:list)
//    		return
//        }

        def user = User.get( params.id )

        if(!user) {
                flash.message = "User not found with id ${params.id}"
                redirect(action:list)
        }
        else {
            return [ user : user ]
        }
    }

    def update = {

//        if (params.id != session.user?.id) {
//    		flash.message = "You can only update yourself"
//    		redirect(action:list)
//    		return
//        }

        def user = User.get( params.id )

        if(user) {
             user.properties = params
            if(user.save()) {
                flash.message = "User ${params.id} updated."
                redirect(action:show,id:user.id)
            }
            else {
                render(view:'edit',model:[user:user])
            }
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def user = new User()
        user.properties = params
        return ['user':user]
    }

    def save = {
        def user = new User()
        user.properties = params
        if(user.save()) {
            flash.message = "user.saved.message"
            flash.args = [ user.firstName, user.lastName]
            flash.defaultMsg = "User Saved"
            redirect(action:show,id:user.id)
        }
        else {
            render(view:'create',model:[user:user])
        }
    }

    def login = {
    }

    def handleLogin = {
        def user = User.findByUserName(params.userName)
		if (!user) {
			flash.message = "User not found for userName: ${params.userName}"
			redirect(action:'login')
			return
		} else {
		    session.user = user
		    //redirect(controller:'event',action:'getMyEvents')
            redirect(action:show,id:user.id)

       	}
	}

    def logout = {
		if(session.user) {
			session.user = null
			redirect(action:'login')
		}
	}

    def addArtist = {
        def user=User.findByUserName(params.userName)
        //def user=session.user
        //user.favArtists.add(params.fav)
        user.addToFavArtists(params.fav);
        if(user.save(flush:true)) {
            flash.message = "user.saved.message"
        //return ['artist':artist]
 
        render(view:'edit',model:[user:user])
        }
    }

    def editArtists = {
        def user=session.user
        return [user:user]
         
    }

    def ajaxDelete = {
      //def user=session.user
      def user=User.findByUserName(session.user.userName)
      user.removeFromFavArtists(params.id)
      if (user.save(flush:true))  {

        //render "Artist ${params.id} deleted"
      }
      else {
	   flash.message = "Artist ${params.id} not found"
	   redirect(action:list)
	  }
	 }
}