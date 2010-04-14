import org.otherobjects.cms.model.*;
import org.otherobjects.cms.types.*;
import org.otherobjects.cms.jcr.dynamic.*;
import org.otherobjects.cms.jcr.*;
import org.otherobjects.cms.util.*;
import org.otherobjects.cms.*;
import groovy.util.slurpersupport.*
import org.apache.commons.lang.StringUtils
import java.util.*
import java.text.*;

//run when logged in!

def file = new File("/home/rcastro/Desktop/xml_export_v1.xml")
def tree = new XmlSlurper().parse(file)
def universalJcrDao=jcr

//get folders
def folders=tree.Folder.findAll{it.property[6].toInteger()==0}
def i=0
while(!folders.isEmpty()){
    def iter=folders.nodeIterator()
    while (iter.hasNext()){
      def cl=iter.next()
      createFolder(cl)
    }
    i++
    folders=tree.Folder.findAll{it.property[6].toInteger()==i}
}

//get images
def images=tree.CmsImage
images.list().each{
  createImage(it)
}

//get articles
def articles=tree.ArticleHandle
articles.list().each{
  createArticle(it)
}

//get index pages
def pages=tree.IndexPageHandle
pages.list().each{
  createIndexPage(it)
}

//get news stories
def news=tree.NewsStoryHandle
news.list().each{
  createNewsStory(it)
}


//
def createFolder(Node obj) {
    String t=obj.children().get(2).text()
    if (t.endsWith("/"))
       t=StringUtils.chop(t)
	def f = jcr.getByPath("/site/"+t)
	if(f) return f;

	f = new SiteFolder();
    f.jcrPath="/site/"+t
    logger.info("path: "+f.jcrPath)
	f.label=obj.children().get(0).text()
	f.code=obj.children().get(5).text()
    //f.url=new Url("/"+obj.children().get(2).text()+obj.children().get(5).text())
    f.publishingOptions = new org.otherobjects.cms.model.PublishingOptions()
    if (!obj.children().get(3).text().equals(""))
       f.publishingOptions.sortOrder=new Long(obj.children().get(3).text())
    if (!obj.children().get(4).text().equals(""))
       f.inMenu=new Boolean(obj.children().get(4).text())
    f.publishingOptions.navigationLabel=obj.children().get(1).text()
    f.defaultPage="index.html"
    f.allowedTypes= ["ArticlePage"]
    f.published=true
	f = jcr.save(f)
	jcr.publish(f, null)
    logger.info("path: "+f.jcrPath)

	return f
}


def createImage(NodeChild obj) {
    def f = jcr.getByPath("/libraries/images/"+obj.property[0].text())
	if(f) return f;

	f = new CmsImage();
  	f.code=obj.property[0].text()
    f.jcrPath="/libraries/images/"+f.code
    logger.info("path: "+f.jcrPath)
	f.label=obj.property[1].text()
    f.description=obj.property[2].text()
    if (!obj.property[5].text().equals(""))
       f.originalWidth=new Long(obj.property[5].text())
    if (!obj.property[6].text().equals(""))
       f.originalHeight=new Long(obj.property[6].text())
    f.thumbnailPath=obj.property[3].text()
    f.originalFileName=obj.property[4].text()
    f.copyright=obj.property[7].text()
	f = jcr.save(f)
	jcr.publish(f, null)
    logger.info("path: "+f.jcrPath)

	return f
}

def createLink(NodeChild obj) {
    //need to format links for name
    def caption= obj.@name.text().replaceAll("[^\\w]","")
    def a = jcr.getByPath("/libraries/links/"+caption)
    if (a) return a;

    a = new DynaNode("CmsLink")
    a.jcrPath = "/libraries/links/"+caption
    a.data.name =  caption
    a.data.code= caption
    a.data.url=obj.text()
    a = jcr.save(a, true)
    logger.error("path: "+a.jcrPath)
    return a

}

def createArticle(NodeChild obj) {
    String path = obj.property[0].text()
    String code= obj.property[9].text()
    if (code.endsWith("/"))
       code=StringUtils.chop(code)
    path="/site/"+path+code
    //logger.info("path: "+path)
    def a = jcr.getByPath(path)
    if (a) return a;

    a = new DynaNode("ArticlePage")
    a.jcrPath=path
    logger.info("path: "+path)

    a.data.title = obj.property[1].text()
    a.data.content = obj.property[3].text()
    a.data.relatedContent = obj.property[4].text()
    a.data.footNotes = obj.property[5].text()
    a.data.code=code
    a.data.relatedLinks=new java.util.ArrayList()
    
    obj.property[6].item.each{
      //logger.warn it.text()
      logger.warn it.@name.text()
      //logger.warn it.class NodeChildren 
      def link=createLink(it)
      a.data.relatedLinks.add(link)
    }
//    a.data.image=createImage(obj.property[],imageDao)
    a.data.publishingOptions = new org.otherobjects.cms.model.PublishingOptions()

    if(!obj.property[7].text().equals(""))
       a.data.publishingOptions.sortOrder=new Long(obj.property[7].text())
    if(!obj.property[8].text().equals(""))
       a.data.inMenu=new Boolean(obj.property[8].text())
    a.data.summary=obj.property[2].text()
    a = jcr.save(a, false)
    if (!a.published)
        jcr.publish(a, null)
    logger.info "published "+path
    return a
}


def createIndexPage(NodeChild obj) {
    String path = obj.property[0].text()
    String code= obj.property[6].text()
    if (code.endsWith("/"))
       code=StringUtils.chop(code)
    path="/site/"+path+code
    //logger.info("path: "+path)
    def a = jcr.getByPath(path)
    if (a) return a;

    a = new DynaNode("IndexPage")
    a.jcrPath=path
    logger.info("path: "+path)

    a.data.title = obj.property[1].text()
    a.data.content = obj.property[2].text()
    a.data.code=code
    //a.data.image=createImage(obj.property[3],imageDao)
    a.data.publishingOptions = new org.otherobjects.cms.model.PublishingOptions()
    a.data.subsidaryTitle1 = obj.property[4].text()
    a.data.subsidaryContent1 = obj.property[5].text()
    a = jcr.save(a, false)
    if (!a.published)
        jcr.publish(a, null)
    logger.info "published "+path
    return a
}

def createNewsStory(NodeChild obj) {
    String code= obj.property[10].text()
    if (code.endsWith("/"))
       code=StringUtils.chop(code)
    def a = jcr.getByPath("/site/news/"+code)
    if (a) return a;
    //logger.info("path: "+path)


    a = new DynaNode("NewsStory")
    a.jcrPath="/site/news/"+code

    a.data.headLine = obj.property[2].text()
    a.data.shortHeadLine = obj.property[3].text()
    a.data.content = obj.property[5].text()
    a.data.summary= obj.property[4].text()
    a.data.relatedContent = obj.property[6].text()
    a.data.code=code
    a.data.relatedLinks=new java.util.ArrayList()
    //obj.property[6].item.each{
      //logger.warn it.text()
      //def link=createLink(it,dao)
      //a.data.relatedLinks.add(link)
    //}
//    a.data.image=createImage(obj.property[7],imageDao)
    a.data.publishingOptions = new org.otherobjects.cms.model.PublishingOptions()
    if(!obj.property[8].text().equals(""))
       a.data.priority=new Boolean(obj.property[8].text())
    if(!obj.property[1].text().equals("")){
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       a.data.storyTimestamp=df.parse(obj.property[1].text())
    }
    a = jcr.save(a, false)
    if (!a.published)
        jcr.publish(a, null)
  logger.info("path: "+ a.jcrPath)
    return a
}