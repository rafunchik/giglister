class User {
  String userName
  String firstName
  String lastName
  String city
  SortedSet <String> favArtists =new TreeSet<String>()
  static hasMany = [favArtists:String]

  static constraints = {
    userName(blank:false,unique:true)
    firstName(blank:false)
    lastName(blank:false)
    city(blank:false)
  }

  String  toString () {
    "$lastName, $firstName"
  }

}
