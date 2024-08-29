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

/**
 * The {@code PageProxy} class provides a proxy representation of a page within a chapter,
 * allowing for the retrieval and download of the page's content.
 * <p>
 * This class offers methods to get the page number, associated chapter, and URL, 
 * as well as to download the page as an {@link java.io.InputStream}, to a specified {@link java.nio.file.Path},
 * or to a {@link java.io.File}. Downloads are handled asynchronously using {@link java.util.concurrent.CompletableFuture}.
 * </p>
 * 
 * @author Hacking Pancakez
 */
public class PageProxy {

    private final String pageNumber;
    private final Chapter chapter;
    private final String url;

    /**
     * Constructs a new {@code PageProxy} with the specified page number, chapter, and URL.
     *
     * @param pageNumber the number of the page as a {@link java.lang.String}
     * @param chapter the associated {@link dev.kurumidisciples.javadex.api.entities.Chapter}
     * @param url the URL of the page as a {@link java.lang.String}
     */
    public PageProxy(String pageNumber, Chapter chapter, String url) {
        this.pageNumber = pageNumber;
        this.chapter = chapter;
        this.url = url;
    }

    /**
     * Returns the page number of this {@code PageProxy}.
     *
     * @return the page number as a {@link java.lang.String}
     */
    public String getPageNumber() {
        return pageNumber;
    }

    /**
     * Returns the associated chapter of this {@code PageProxy}.
     *
     * @return the associated {@link dev.kurumidisciples.javadex.api.entities.Chapter}
     */
    public Chapter getChapter() {
        return chapter;
    }

    /**
     * Returns the URL of this page.
     *
     * @return the URL as a {@link java.lang.String}
     */
    public String getUrl() {
        return url;
    }

    /**
     * Downloads the content of this page and returns it as an {@link java.io.InputStream}.
     *
     * @return an {@link java.io.InputStream} containing the page data
     * @throws java.io.IOException if an I/O error occurs during the download process
     */
    public InputStream download() throws IOException {
        URL url = new URL(getUrl());
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    /**
     * Asynchronously downloads the content of this page to the specified {@link java.nio.file.Path}.
     * <p>
     * The downloaded content is saved as a JPEG file, with the filename based on the page number.
     * </p>
     *
     * @param path the path where the page will be downloaded
     * @return a {@link java.util.concurrent.CompletableFuture} that completes with the path to the downloaded file
     * @throws java.util.concurrent.CompletionException if an I/O error occurs during the download process
     */
    public CompletableFuture<Path> downloadToPath(Path path) throws CompletionException {
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
     * Asynchronously downloads the content of this page to the specified {@link java.io.File}.
     * <p>
     * The downloaded content replaces the contents of the provided file.
     * </p>
     *
     * @param file the file where the page will be downloaded
     * @return a {@link java.util.concurrent.CompletableFuture} that completes with the downloaded file
     * @throws java.util.concurrent.CompletionException if an I/O error occurs during the download process
     */
    public CompletableFuture<File> downloadToFile(File file) throws CompletionException {
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
