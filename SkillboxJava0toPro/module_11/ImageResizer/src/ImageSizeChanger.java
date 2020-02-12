import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;

public class ImageSizeChanger implements Callable<Integer> {
  private String dstFolder;
  private File[] files;
  private int newWidth;
  private long start;

  public ImageSizeChanger(String dstFolder, File[] files, int newWidth) {
    this.dstFolder = dstFolder;
    this.files = files;
    this.newWidth = newWidth;
    start = System.currentTimeMillis();
  }

  @Override
  public Integer call() {
    try {
      for (File file : files) {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
          continue;
        }

        BufferedImage resizedImage = getResizedBufferedImage(image);

        File newFile = new File(dstFolder + "/" + file.getName());
        ImageIO.write(resizedImage, "jpg", newFile);
      }

      long finish = System.currentTimeMillis() - start;
      System.out.println(
          "\t\t"
              + Thread.currentThread().getName()
              + String.format(
                  " executed by %02d:%02d:%02d",
                  finish / 1000 / 3600, finish / 1000 / 60 % 60, finish / 1000 % 60));
    } catch (Exception ex) {
      ex.printStackTrace();
      return 0;
    }

    return 1;
  }

  private BufferedImage getResizedBufferedImage(BufferedImage image) {
    int tmpImgWidth = newWidth * 2;
    int tmpImgHeight =
        (int) Math.round(image.getHeight() / (image.getWidth() / (double) tmpImgWidth));
    int newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));

    BufferedImage tmpImg = new BufferedImage(tmpImgWidth, tmpImgHeight, image.getType());
    double sizeFactor = (double) tmpImgWidth / image.getWidth();
    AffineTransform tr = AffineTransform.getScaleInstance(sizeFactor, sizeFactor);
    AffineTransformOp op = new AffineTransformOp(tr, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    op.filter(image, tmpImg);

    BufferedImage newImg = new BufferedImage(newWidth, newHeight, image.getType());
    sizeFactor = (double) newWidth / tmpImg.getWidth();
    tr = AffineTransform.getScaleInstance(sizeFactor, sizeFactor);
    op = new AffineTransformOp(tr, AffineTransformOp.TYPE_BICUBIC);
    op.filter(tmpImg, newImg);

    return newImg;
  }
}
