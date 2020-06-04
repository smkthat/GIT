import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinkFinderAction extends RecursiveAction {
  private final String url;
  private final LinkHandler lh;
  public static AtomicInteger linksFoundCounter = new AtomicInteger(0);

  public LinkFinderAction(String url, LinkHandler lh) {
    this.url = url;
    this.lh = lh;
  }

  @Override
  protected void compute() {
    if (LinksFounder.MAX_VISITED_LINKS_LENGTH != -1) {
      if (lh.size() >= LinksFounder.MAX_VISITED_LINKS_LENGTH) {
        if (getPool().getQueuedTaskCount() == 0) {
          getPool().shutdown();
        }

        return;
      }
    }

    if (isValidToVisit(url)) {
      try {
        List<RecursiveAction> actions = new ArrayList<>();
        URL urlLink = new URL(url);

        Parser parser = new Parser(urlLink.openConnection());
        NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

        IntStream.range(0, list.size()).mapToObj(i -> (LinkTag) list.elementAt(i))
            .collect(Collectors.toSet()).stream()
            .map(LinkTag::extractLink)
            .filter(this::isValidToVisit)
            .forEach(
                link -> {
                  actions.add(new LinkFinderAction(link, lh));
                  linksFoundCounter.getAndAdd(1);
                });

        lh.addVisited(url);
        invokeAll(actions);
      } catch (Exception e) {
        // ignore 404, unknown protocol or other server errors
      }
    }
  }

  private boolean isValidToVisit(String url) {
    return url.startsWith(LinksFounder.STARTING_URL)
        && !lh.isVisited(url)
        && !isFileUrl(url)
        && !url.contains("#")
        && !url.contains("?");
  }

  private boolean isFileUrl(String url) {
    Pattern fileFilter =
        Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
                + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    return fileFilter.matcher(url).matches();
  }
}
