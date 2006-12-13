/*
 * Created on 8 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.games.gui.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureClassLoader;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarLoader extends SecureClassLoader {
	private URL urlBase;	
	public boolean printLoadMessages = true;
	Hashtable classArrays;

	public JarLoader(String base, ClassLoader parent) {
		super(parent);
		try {
			if (!(base.endsWith("/")))
				base = base + "/";
			urlBase = new URL(base);
			classArrays = new Hashtable();
		} catch (Exception e) {
			throw new IllegalArgumentException(base);
		}
	}

	private byte[] getClassBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		boolean eof = false;
		while (!eof) {
			try {
				int i = bis.read();
				if (i == -1)
					eof = true;
				else baos.write(i);
			} catch (IOException e) {
				return null;
			}
		}
		return baos.toByteArray();
	}

	protected Class findClass(String name) {
		String urlName = name.replace('.', '/');
		byte buf[];
		Class cl;

		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			int i = name.lastIndexOf('.');
			if (i >= 0)
				sm.checkPackageDefinition(name.substring(0, i));
		}

		buf = (byte[]) classArrays.get(urlName);
		if (buf != null) {
			cl = defineClass(name, buf, 0, buf.length);
			return cl;
		}

		try {
			URL url = new URL(urlBase, urlName + ".class");
			if (printLoadMessages)
				System.out.println("Loading " + url);
			InputStream is = url.openConnection().getInputStream();
			buf = getClassBytes(is);
			cl = defineClass(name, buf, 0, buf.length);
			return cl;
		} catch (Exception e) {
			System.out.println("Can't load " + name + ": " + e);
			return null;
		}
	}

	public void readJarFile(String name) {
		URL jarUrl = null;
		JarInputStream jis;
		JarEntry je;

		try {
			jarUrl = new URL(urlBase, name);
		} catch (MalformedURLException mue) {
			System.out.println("Unknown jar file " + name);
			return;
		}
		if (printLoadMessages)
			System.out.println("Loading jar file " + jarUrl);

		try {
			jis = new JarInputStream(
							jarUrl.openConnection().getInputStream());
		} catch (IOException ioe) {
			System.out.println("Can't open jar file " + jarUrl);
			return;
		}

		try {
			while ((je = jis.getNextJarEntry()) != null) {
				String jarName = je.getName();
				if (jarName.endsWith(".class"))
					loadClassBytes(jis, jarName);
				// else ignore it; it could be an image or audio file
				jis.closeEntry();
			}
		} catch (IOException ioe) {
			System.out.println("Badly formatted jar file");
		}
	}

	private void loadClassBytes(JarInputStream jis, String jarName) {
		if (printLoadMessages)
			System.out.println("\t" + jarName);
		BufferedInputStream jarBuf = new BufferedInputStream(jis);
		ByteArrayOutputStream jarOut = new ByteArrayOutputStream();
		int b;
		try {
			while ((b = jarBuf.read()) != -1)
				jarOut.write(b);
			classArrays.put(jarName.substring(0, jarName.length() - 6),
							jarOut.toByteArray());
		} catch (IOException ioe) {
			System.out.println("Error reading entry " + jarName);
		}
	}

	public void checkPackageAccess(String name) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkPackageAccess(name);
	}
}