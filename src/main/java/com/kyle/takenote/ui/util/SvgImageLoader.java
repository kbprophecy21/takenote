package com.kyle.takenote.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javafx.scene.image.Image;

/**
 * Renders an SVG (resource) into a JavaFX Image using Apache Batik.
 * Caches rendered results by (path,width,height).
 */
public final class SvgImageLoader {

    private SvgImageLoader() {}

    private static final Map<String, Image> CACHE = new ConcurrentHashMap<>();

    public static Image loadSvgAsImage(String resourcePath, double widthPx, double heightPx) {
        String key = resourcePath + "|" + widthPx + "x" + heightPx;

        Image cached = CACHE.get(key);
        if (cached != null) return cached;

        try (InputStream svgStream = SvgImageLoader.class.getResourceAsStream(resourcePath)) {
            if (svgStream == null) {
                throw new IllegalArgumentException("SVG resource not found: " + resourcePath);
            }

            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) widthPx);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) heightPx);

            ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(pngOut);

            transcoder.transcode(input, output);

            byte[] pngBytes = pngOut.toByteArray();
            Image fxImage = new Image(new ByteArrayInputStream(pngBytes));

            CACHE.put(key, fxImage);
            return fxImage;

        } catch (Exception e) {
            throw new RuntimeException("Failed to render SVG: " + resourcePath, e);
        }
    }
}
