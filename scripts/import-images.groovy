import org.otherobjects.cms.model.*;
import org.otherobjects.cms.types.*;
import org.otherobjects.cms.jcr.dynamic.*;
import org.otherobjects.cms.jcr.*;
import org.otherobjects.cms.util.*;
import org.otherobjects.cms.*;
import org.otherobjects.cms.tools.CmsImageTool;
import java.util.*
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import org.apache.commons.io.FileUtils



//run when logged in!
def imagesPath="/home/rcastro/import/images/"
def pattern  = ".*(\\.png|\\.jpg)"
def universalJcrDao=jcr


def images = getImages()
logger.info "size "+ images.size()
//writeImages(images)
def i=0
images.entrySet().each{
  createImage(it.key,it.value)
  i++
}

logger.info "imported images "+i

def getImages()
{
  def images=[:]
  def dir=new File( "/home/rcastro/workspace-oocms2/www.rlsb.org.uk/webapp/public-data/images/originals")
  dir.mkdirs()
  logger.debug "exists "+dir.exists()
  new File( "/home/rcastro/import/images/" ).eachFile() {
        f ->
    FileUtils.copyDirectory(new File( "/home/rcastro/import/images/" ),dir,true)
    // create image node
    images.put(f.getName(), ImageIO.read(f))
  }
  return images
}


def createImage(String fileName, BufferedImage image) {
    def f = jcr.getByPath("/libraries/images/"+fileName)
	if(f) return f;
  	f = new CmsImage();
  	f.code=fileName
    f.jcrPath="/libraries/images/"+f.code
	f.label=f.code
    f.originalWidth=new Long(image.width)
    f.originalHeight=new Long(image.height)
    f.originalFileName=f.code
    f = jcr.save(f)
    jcr.publish(f, null)
    def cmsImageTool=  app.getBean("cmsImageTool")
    cmsImageTool.getThumbnail(f)
  return f
}

