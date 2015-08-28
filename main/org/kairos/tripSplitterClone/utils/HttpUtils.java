package org.kairos.tripSplitterClone.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP utility methods.
 *
 * Created on 8/24/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class HttpUtils {

	/**
	 * Unzips a GZipped request.
	 * 
	 * @param request
	 *            the request to unzip
	 * 
	 * @return the unzipped string body
	 */
	public static String getUnzippedBody(HttpServletRequest request) {
		try {
			return gZippedInputStreamToString(request.getInputStream(),
					Boolean.FALSE, "UTF-8");
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Transforms a GZipped input stream into a unzipped string.
	 * 
	 * @param is
	 *            the input stream to decode
	 * @param urlDecode
	 *            if the unzipped body needs to be decoded from the URL escaping
	 *            characters
	 * @param encoding
	 *            encoding to use
	 * 
	 * @return decoded string
	 */
	private static String gZippedInputStreamToString(InputStream is,
			Boolean urlDecode, String encoding) {
		try {
			GZIPInputStream gis = new GZIPInputStream(is);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					gis));
			StringBuffer sb = new StringBuffer();
			String line = null;

			while ((line = bReader.readLine()) != null) {
				sb.append(line);
			}

			String content = sb.toString();

			if (urlDecode) {
				return URLDecoder.decode(content, encoding);
			} else {
				return content;
			}
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Extracts the language tag to use from the request.
	 * 
	 * @param request the request from where to extract the locale
	 * @param defaultLanguageTag the default language tag
	 * @param specific if the extraction must be specific or not
	 * 
	 * @return language tag
	 */
	public static String extractLanguageTag(HttpServletRequest request, String defaultLanguageTag, Boolean specific) {
		try {
			// gets the preferred language tag
			String tag = request.getHeader("Accept-Language").split(",")[0].split(";")[0];
			if (!specific) {
				// we strip the specific part
				tag = tag.split("-")[0].split("_")[0];
			}
			return tag;
		} catch (Exception e) {
			// we couldn't extract the language tag, returing default 
			return defaultLanguageTag;
		}
	}
}
