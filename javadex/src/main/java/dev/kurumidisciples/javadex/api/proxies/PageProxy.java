package dev.kurumidisciples.javadex.api.proxies;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import dev.kurumidisciples.javadex.api.entities.Chapter;

public class PageProxy {

  private String pageNumber;
  private Chapter chapter;
  private String url;

 public PageProxy(String pageNumber, Chapter chapter, String url) {
   this.pageNumber = pageNumber;
   this.chapter = chapter;
   this.url = url;
 }

  public String getPageNumber() {
    return pageNumber;
  }

  public Chapter getChapter() {
    return chapter;
  }

  public String getUrl() {
    return url;
  }
  
  /**
   * Downloads the page.
   * @return An input stream containing the page data.
   * @throws IOException If an I/O error occurs during the download process.
   */
   public InputStream download() throws IOException {
    URL url = new URL(getUrl());
    URLConnection conn = url.openConnection();
    return conn.getInputStream();
  }

  /**
   * Downloads the page to a path.
   * @param path The path to download the page to.
   * @return A future that completes with the path.
   * @throws CompletionException If an I/O error occurs during the download process.
  */
  public CompletableFuture<Path> downloadToPath(Path path) throws CompletionException{
  return CompletableFuture.supplyAsync(() -> {
    try {
      InputStream in = download();
      Path filePath = path.resolve(pageNumber + ".jpg");
      Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
      return filePath;
    } catch (Exception e) {
      throw new CompletionException(e);
    }
  });
}
  /**
   * Downloads the page to a file.
   * @param file The file to download the page to.
   * @return A future that completes with the file.
   * @throws CompletionException If an I/O error occurs during the download process.
  */
  public CompletableFuture<File> downloadToFile(File file) throws CompletionException{
  return CompletableFuture.supplyAsync(() -> {
    try {
      InputStream in = download();
      Path filePath = file.toPath();
      Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
      return file;
    } catch (Exception e) {
      throw new CompletionException(e);
    }
  });
}
}