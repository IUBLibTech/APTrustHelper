package edu.indiana.dlib.aptrust;

import java.awt.*;
import java.io.*;
import java.util.prefs.Preferences;
import java.util.zip.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import org.xeustechnologies.jtar.*;

public class APTrustMain extends JFrame {
	
	public static final int WIDTH = 700;
	public static final int HEIGHT = 500;
	public static final String PREFS_NOVALUE = "~Nope~";
	public static final String PREFS_NAME = "IU_APTRUST_HELPER";
	public static final String PREF_BAGIT_LOC = "Bagit_Location";
	public static final String PREF_APTRUST_LOC = "APTrust_Apps_Location";
	public static final String PREF_INST_PREFIX = "APTrust_Institutional_Prefix";
// TODO --- add pref, button and field for config file loc
	public static Preferences prefs;
	private JFileChooser fileChooser = new JFileChooser();
	private JPanel pnlTop;
	private JPanel pnlBottom;
	private JPanel contentPane = null;
	private JPanel pnlFileChoose;
	private JLabel lblFileChoose;
	private JTextField txtfFileChoose;
	private JPanel pnlInstChoose;
	private JLabel lblInstChoose;
	private JTextField txtfInstChoose;
	private JButton btnFileChoose;
	private JPanel pnlActionButtons;
	private JButton btnUpload;
	private JButton btnShowPending;
	private JButton btnSetBagitLoc;
	private JButton btnSetAPTrustAppsLoc;
	private JTextArea txt_fileinfo;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, "Error setting look and feel.");
		}
		APTrustMain.prefs = Preferences.userRoot().node(APTrustMain.PREFS_NAME);
		new APTrustMain();
	}
	
	public APTrustMain() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("AP Trust Helper");
        contentPane = new JPanel();
        this.setContentPane(contentPane);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        pnlTop = new JPanel();
        BorderLayout b1 = new BorderLayout();
        contentPane.setLayout(b1);
        this.setMinimumSize(new Dimension(700, 400));
        contentPane.add(pnlTop, BorderLayout.NORTH);
        pnlTop.setLayout(new GridLayout(0, 1));
        pnlFileChoose = new JPanel();
        FlowLayout f2 = new FlowLayout();
        f2.setAlignment(FlowLayout.LEFT);
        pnlFileChoose.setLayout(f2);
        lblFileChoose = new JLabel("File to process:");
        txtfFileChoose = new JTextField();
        txtfFileChoose.setPreferredSize(new Dimension(490, 25));
        btnFileChoose = new JButton("Browse");
        pnlFileChoose.add(lblFileChoose);
        pnlFileChoose.add(txtfFileChoose);
        pnlFileChoose.add(btnFileChoose);
        btnFileChoose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int response = fileChooser.showOpenDialog(null);
				if (response != JFileChooser.CANCEL_OPTION) {
					File x = fileChooser.getSelectedFile();
					txtfFileChoose.setText(x.getPath());
				}	
			}
        });
        pnlTop.add(pnlFileChoose);        
        pnlInstChoose = new JPanel();
        FlowLayout f3 = new FlowLayout();
        f3.setAlignment(FlowLayout.LEFT);
        pnlInstChoose.setLayout(f3);
        lblInstChoose = new JLabel("Institutional Prefix for Bags");
        txtfInstChoose = new JTextField();
        txtfInstChoose.setPreferredSize(new Dimension(75, 25));
        pnlInstChoose.add(lblInstChoose);
        pnlInstChoose.add(txtfInstChoose);
        pnlTop.add(pnlInstChoose);
        pnlActionButtons = new JPanel();
        FlowLayout f4 = new FlowLayout();
        f4.setAlignment(FlowLayout.LEFT);
        pnlActionButtons.setLayout(f4);
        btnUpload = new JButton("Upload");
        pnlActionButtons.add(btnUpload);
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Runnable q = new Runnable() {
					public void run() {
						sendToTest(txtfFileChoose.getText());
				    }
				};
				Thread t = new Thread(q);
				t.start();	
			}
        });
        btnShowPending = new JButton("Show Pending Bundles");
        pnlActionButtons.add(btnShowPending);
        btnShowPending.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Runnable q = new Runnable() {
					public void run() {
						showPending();
				    }
				};
				Thread t = new Thread(q);
				t.start();	
			}
        });
        btnSetBagitLoc = new JButton("Bagit Loc");
        pnlActionButtons.add(btnSetBagitLoc);
        btnSetBagitLoc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setBagitLoc();	
			}
        });
        btnSetAPTrustAppsLoc = new JButton("APTrust Apps Loc");
        pnlActionButtons.add(btnSetAPTrustAppsLoc);
        btnSetAPTrustAppsLoc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				setAPTrustAppsLoc();	
			}
        });
        pnlTop.add(pnlActionButtons);
        pnlTop.setPreferredSize(new Dimension(210, 105));
        txt_fileinfo = new JTextArea(13, 24);
        txt_fileinfo.setEditable(false);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        txt_fileinfo.setFont(font);
        JScrollPane scrollPane = new JScrollPane(txt_fileinfo);
        txt_fileinfo.setText("AP Trust Upload Helper\n\n");
        pnlBottom = new JPanel();
        pnlBottom.setLayout(new BorderLayout());
        pnlBottom.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(pnlBottom, BorderLayout.CENTER);
        this.setSize(WIDTH, HEIGHT);
        //set up paths to external apps
        if (prefs.get(PREF_BAGIT_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) { 
			txt_fileinfo.append("NOTE: Bagit location not found. Use 'Bagit Loc' button above to find it before continuing, or download it from https://github.com/LibraryOfCongress/bagit-java.\n");
		}
        if (prefs.get(PREF_APTRUST_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) { 
			txt_fileinfo.append("NOTE: APTrust apps location not found. Use 'APTrust Apps Loc' button above to find it before continuing, or download from https://sites.google.com/a/aptrust.org/aptrust-wiki/home/partner-tools.\n");
		}
        //grab existing info, if present
        if (!prefs.get(PREF_INST_PREFIX, PREFS_NOVALUE).equals(PREFS_NOVALUE)) { 
			txtfInstChoose.setText(prefs.get(PREF_INST_PREFIX, PREFS_NOVALUE));
		}
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
        	frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
        	frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);
	}
	
	private void setBagitLoc() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fileChooser.showOpenDialog(null);
		if (response != JFileChooser.CANCEL_OPTION) {
			File x = fileChooser.getSelectedFile();
			//test to see if 'bag' command works here
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec(x.getAbsolutePath() + File.separator + "bag.bat create --help");
			} catch (IOException err) {
				err.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error: Bag command is not runnable from this location.", "Bag command not found", JOptionPane.ERROR_MESSAGE);
				return;
			} 
			txt_fileinfo.append("New Bagit location specified: " + x.getAbsolutePath());
			//create new pref for this for next time
			prefs.put(PREF_BAGIT_LOC, x.getAbsolutePath());
		}	
	}
	
	private void showPending() {
// TODO implement
	}
	
	private void setAPTrustAppsLoc() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fileChooser.showOpenDialog(null);
		if (response != JFileChooser.CANCEL_OPTION) {
			File x = fileChooser.getSelectedFile();
			//test to see if verify command works here
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec(x.getAbsolutePath() + File.separator + "apt_validate --help");
			} catch (IOException err) {
				err.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error: APTrust applications not found.", "AP Trust applications not found", JOptionPane.ERROR_MESSAGE);
				return;
			} 
			txt_fileinfo.append("New APTrust apps location specified: " + x.getAbsolutePath());
			//create new pref for this for next time
			prefs.put(PREF_APTRUST_LOC, x.getAbsolutePath());
		}	
	}
	
	private void sendToTest(String file) {
		//run some basic checks first
		if (prefs.get(PREF_BAGIT_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) { 
			txt_fileinfo.append("ERROR: Bagit location unknown. Use 'Bagit Loc' button above to find it before continuing.\n");
			return;
		}
		if (prefs.get(PREF_APTRUST_LOC, PREFS_NOVALUE).equals(PREFS_NOVALUE)) { 
			txt_fileinfo.append("ERROR: APTrust location unknown. Use 'APTrust Apps Loc' button above to find it before continuing.\n");
			return;
		}
		//remember institutional setting
		prefs.put(PREF_INST_PREFIX, txtfInstChoose.getText());
		this.btnUpload.setEnabled(false);
		/* ======== STEP 1 - FIND AND UNZIP FILP ============== */
		txt_fileinfo.append("Starting upload process for file: " + file + "\n");
		File theFile = new File(file);
		if (!theFile.exists() || !theFile.isFile()) {
			txt_fileinfo.append("ERROR: Invalid file specified\n");
			this.btnUpload.setEnabled(true);
			return;
		}
		if (!theFile.getName().toLowerCase().endsWith("zip")) {
			txt_fileinfo.append("ERROR: File must have the 'zip' extension\n");
			this.btnUpload.setEnabled(true);
			return;
		}
		File newFolder = new File(theFile.getParent() + File.separator + theFile.getName().substring(0, theFile.getName().length() - 4));
		int x = 0;
		while (newFolder.exists()) {
			newFolder = new File(newFolder.getPath() + "-" + x);
			x = x + 1;
		}
		try {
			newFolder.mkdirs();
			unzip(theFile, newFolder);
		} catch (Exception err) {
			txt_fileinfo.append("ERROR UNZIPPING SOURCE FILE\n");
			err.printStackTrace();
			this.btnUpload.setEnabled(true);
			return;
		}
		txt_fileinfo.append("Successfully unzipped input file to " + theFile.getParentFile().getPath() + "\n");
		/* ======== STEP 2 - CREATE BAG ============== */
		File folderWeNeed = null;
		boolean keepgoing = true;
		File[] fils = newFolder.listFiles();
		//assume a single, flat folder with all the content in it for a single DSpace item
		while (keepgoing) {
			if (fils.length == 0) {
				this.btnUpload.setEnabled(true);
				txt_fileinfo.append("ERROR - ZIP FILE HAS NO CONTENT\n");
				return;
			}
			if (fils[0].isDirectory()) {
				fils = fils[0].listFiles();
			} else {
				folderWeNeed = fils[0].getParentFile();
				keepgoing = false;
			}
		}
		txt_fileinfo.append("Found content folder: " + folderWeNeed.getPath() + "\n");		
		//read handle file to grab handle
		int xx = 0;
		File handleFile = null;
		fils = folderWeNeed.listFiles();
		while (xx < fils.length) {
			if (fils[xx].getName().equals("handle")) {
				//handle file found
				handleFile = fils[xx];
			}
			xx++;
		}
		if (handleFile == null) {
			txt_fileinfo.append("ERROR: No handle file found\n");
			this.btnUpload.setEnabled(true);
			return;
		}
		String handle;
		try {
			BufferedReader firstLine = new BufferedReader(new FileReader(handleFile));
		    handle = firstLine.readLine();		
		    firstLine.close();
		    //can't have slashes in a directory name
		    handle = handle.replace("/", "-");
		} catch (IOException err) {
			txt_fileinfo.append("ERROR READING HANDLE FILE\n");
			err.printStackTrace();
			this.btnUpload.setEnabled(true);
			return;
		}
	    txt_fileinfo.append("HANDLE PARSED: " + handle + "\n");
		File baggedFolder = new File(newFolder.getParent() + File.separator + txtfInstChoose.getText() + "." + handle);
		if  (baggedFolder.exists()) {
			txt_fileinfo.append("ERROR: Bag folder already exists\n");
			this.btnUpload.setEnabled(true);
			return;
		}		
		try {
			txt_fileinfo.append("RUNNING BAGIT COMMAND\n");
			ProcessBuilder builder = new ProcessBuilder(
	            "cmd.exe", "/c", "bag.bat create \"" + baggedFolder + "\" \"" + folderWeNeed.getAbsolutePath() + File.separator + "*\"");
			builder.directory(new File(prefs.get(PREF_BAGIT_LOC, PREFS_NOVALUE)));
			builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            txt_fileinfo.append(line + "\n");
	        }
		} catch (IOException err) {
			err.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error running bag command.", "Bag command error", JOptionPane.ERROR_MESSAGE);
			this.btnUpload.setEnabled(true);
			return;
		}
// TODO --- create info file --- need a new field for bag name and combo box for restriction
		File tarFile;
		try {
			tarFile = new File(baggedFolder.getParent() + File.separator + baggedFolder.getName() + ".tar");
			if (tarFile.exists()) {
				txt_fileinfo.append("ERROR tar file already exists\n");
				this.btnUpload.setEnabled(true);
				return;
			}
			FileOutputStream dest = new FileOutputStream(tarFile);
	        TarOutputStream out = new TarOutputStream( new BufferedOutputStream( dest ) );
	        tarFolder( null, baggedFolder.getAbsolutePath(), out );
	        out.close();
		} catch (Exception err) {
			err.printStackTrace();
			txt_fileinfo.append("ERROR creating tar file\n");
			this.btnUpload.setEnabled(true);
			return;
		}
		/* ======== STEP 3 - VERIFY BAG ============== */
		txt_fileinfo.append("VERIFYING BAG\n");
		try {
			ProcessBuilder builder = new ProcessBuilder(
	            "cmd.exe", "/c", "apt_validate create \"" + tarFile);
	        builder.directory(new File(prefs.get(PREF_APTRUST_LOC, PREFS_NOVALUE)));
			builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        boolean failed = false;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            if (line.startsWith("[FAIL]")) {
	            	failed = true;
	            }
	            txt_fileinfo.append(line + "\n");
	        }
	        if (failed) {
	        	txt_fileinfo.append("INVALID BAG ERROR --- SCRIPT WILL TERMINATE");
				this.btnUpload.setEnabled(true);
				return;
	        }
		} catch (IOException err) {
			err.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error running bag validate command.", "Validate command error", JOptionPane.ERROR_MESSAGE);
			this.btnUpload.setEnabled(true);
			return;
		}
		/* ======== STEP 4 - UPLOAD BAG ============== */
// TODO - IMPLEMENT
		this.btnUpload.setEnabled(true);
	}
		
	private void unzip(File zippedFile, File locationForUnzip) throws IOException {
		ZipFile zipFile = new ZipFile(zippedFile, ZipFile.OPEN_READ);
	    Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
	    while (zipFileEntries.hasMoreElements()) {
	    	ZipEntry entry = (ZipEntry)zipFileEntries.nextElement();
	        String currentEntry = entry.getName();
	        File destFile = new File(locationForUnzip, currentEntry);
	        File destinationParent = destFile.getParentFile();
	        destinationParent.mkdirs();
	        if (!entry.isDirectory()) {
	        	BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
	            int currentByte;
	            byte data[] = new byte[2048];
	            FileOutputStream fos = new FileOutputStream(destFile);
	            BufferedOutputStream dest = new BufferedOutputStream(fos, 2048);
	            while ((currentByte = is.read(data, 0, 2048)) != -1) {
	            	dest.write(data, 0, currentByte);
	            }
	            dest.flush();
	            dest.close();
	            is.close();
	        }
	    }
	    zipFile.close();
	}
	
	//see https://code.google.com/p/jtar/source/browse/trunk/jtar/src/test/java/org/xeustechnologies/jtar/JTarTest.java
	public void tarFolder(String parent, String path, TarOutputStream out) throws IOException {
        BufferedInputStream origin = null;
        File f = new File( path );
        String files[] = f.list();
        if (files == null) {
            files = new String[1];
            files[0] = f.getName();
        }
        parent = ( ( parent == null ) ? ( f.isFile() ) ? "" : f.getName() + "/" : parent + f.getName() + "/" );
        for (int i = 0; i < files.length; i++) {
            System.out.println( "Adding: " + files[i] );
            File fe = f;
            byte data[] = new byte[2048];
            if (f.isDirectory()) {
                fe = new File( f, files[i] );
            }
            if (fe.isDirectory()) {
                String[] fl = fe.list();
                if (fl != null && fl.length != 0) {
                    tarFolder( parent, fe.getPath(), out );
                } else {
                    TarEntry entry = new TarEntry( fe, parent + files[i] + "/" );
                    out.putNextEntry( entry );
                }
                continue;
            }
            FileInputStream fi = new FileInputStream( fe );
            origin = new BufferedInputStream( fi );
            TarEntry entry = new TarEntry( fe, parent + files[i] );
            out.putNextEntry( entry );
            int count;
            while (( count = origin.read( data ) ) != -1) {
                out.write( data, 0, count );
            }
            out.flush();
            origin.close();
        }
    }	
}
