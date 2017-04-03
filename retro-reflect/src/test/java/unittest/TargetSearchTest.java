package unittest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import image.processing.HsbFilter;
import image.processing.Searcher;
import image.processing.exception.NotFound;
import image.processing.finder.Figure;
import image.processing.finder.Finder;
import image.processing.profile.TargetProfile;

public class TargetSearchTest {
	static final List<BufferedImage> sampleImages;
	static final List<String> sampleFileNames;
	static {
		sampleImages = new ArrayList<>();
		sampleFileNames = new ArrayList<>();
		String imagePath = getImagePath();
		final File[] files = new File(imagePath).listFiles();
		for (final File file : files) {
			if (file.isFile()) {
				try {
					sampleImages.add(ImageIO.read(file));
					sampleFileNames.add(file.getName());
				} catch (final IOException e) {
				}
			}
		}
	}

	private static String getImagePath() {
		String relativePath = "image-samples\\Vision Images\\LED Peg";
		String rootPath = new File(TargetSearchTest.class.getClassLoader().getResource(relativePath).getFile())
				.getParent();
		String pathToImages = rootPath + '\\' + relativePath;
		return pathToImages.replaceAll("%20", " ");
	}

	public static BufferedImage getImage(int index) {
		return sampleImages.get(index);
	}

	public static String getImageFileName(int index) {
		return sampleFileNames.get(index);
	}

	public static int[] getImageIdentifiers() {
		final int[] answer = new int[sampleImages.size()];
		int count = 0;
		for (@SuppressWarnings("unused")
		final BufferedImage bufferedImage : sampleImages) {
			answer[count] = count++;
		}
		return answer;
	}

	@Test
	public void getImageFileNames() {
		final File folder = new File(getImagePath());
		final File[] listOfFiles = folder.listFiles();
		final StringBuilder b = new StringBuilder();
		for (final File file : listOfFiles) {
			b.append(file.getName().split("\\.")[0] + ", ");
		}
		System.out.println(b);
	}

	private TargetProfile getTargetProfile() {
		return TargetProfile.goodReflection();
	}

	// @Test
	// public void testAnalysis() {
	// final BufferedImage img = getImage(0);
	// Analyzer.analyze(img, 0, 0, img.getWidth(), img.getHeight(),
	// getTargetProfile());
	// }
	//
	// @Test
	// public void testAnalysisFocused() {
	// Analyzer.analyze(getImage(0), 468, 155, 173, 123, getTargetProfile());
	// }

	@Test
	public void testTargetFinder() {
		final BufferedImage image = getImage(0);
		final HsbFilter hsbFilter = new HsbFilter(image);
		hsbFilter.setRememberPath(true);
		try {
			final Finder finder = new Searcher(hsbFilter, getTargetProfile(),
					new Point(image.getWidth() / 2, image.getHeight() / 2), Searcher.FigureType.Hopper).finder();
			for (final Figure figure : finder.figures) {
				figure.showCorners();
				figure.showTargetCorners(Color.RED);
			}
		} catch (final NotFound e) {
			System.out.println("testTargetFinder -> NotFound");
		}
		hsbFilter.displayAccessPath();
		try {
			Thread.sleep(10000);
		} catch (final InterruptedException e) {
		}
	}

	@Test
	public void testTargetFinderAllFiles() {
		final JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(800, 500);
		final JLabel title = new JLabel();
		frame.add(title);
		final JLabel lbl = new JLabel();
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (final int i : getImageIdentifiers()) {
			final BufferedImage image = getImage(i);
			final HsbFilter hsbFilter = new HsbFilter(image);
			hsbFilter.setRememberPath(true);
			try {
				final Finder finder = new Searcher(hsbFilter, getTargetProfile(),
						new Point(image.getWidth() / 2, image.getHeight() / 2), Searcher.FigureType.Hopper).finder();
				for (final Figure figure : finder.figures) {
					figure.showCorners();
					figure.showTargetCorners(Color.RED);
				}
				title.setText("" + i + ".jpg");
			} catch (final NotFound e) {
			} catch (final Exception e) {
				e.printStackTrace();
			}
			hsbFilter.drawAccessPath();
			final ImageIcon icon = new ImageIcon(image);
			lbl.setIcon(icon);
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
			}
		}
	}

	@Test
	public void testTargetProfile() {
		final TargetProfile profile = new TargetProfile(0.67f, 0.82f, 0.5f, 0.9f, 0.3f, 0.7f);
		assertTrue(profile.matches(new float[] { 0.7f, 0.7f, 0.7f }));
		assertFalse(profile.matches(new float[] { 0.7f, 0.7f, 0.8f }));
		assertFalse(profile.matches(new float[] { 0.7f, 0.4f, 0.7f }));
		assertFalse(profile.matches(new float[] { 0.65f, 0.7f, 0.7f }));
	}

}
