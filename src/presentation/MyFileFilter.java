
package presentation;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author bat
 */
public class MyFileFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
        return !f.isHidden() && (f.getName().endsWith(".txt") || f.isDirectory());
    }

    @Override
    public String getDescription() {
        return "Archivos de texto (no ocultos)";
    }
    
}
