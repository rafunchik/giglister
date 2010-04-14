def file=new File("/home/rcastro/workspace/www.ecb.co.uk-rebuild/data/stats")

def slurper=new XmlSlurper()
file.eachFileRecurse{ f->
    if (!f.name.endsWith("xml") || f.isDirectory()) return
    f.withReader{ ff->
      if (!ff.readLine() || ff.readLine().contains("<!DOCTYPE HTML PUBLIC"))  {
         def ss=f.delete()
        println ss
      }
    }
    if (f.exists())
    {
      def obj=slurper.parse(f)
      if(obj.Result && (obj.Result.@type.text().equalsIgnoreCase("Loss") || obj.Result.@type.text().equalsIgnoreCase("Win") || obj.Result.@type.text().equalsIgnoreCase("Draw")))
      {
         def g=f.delete()
        println g
      }
    }
}