/**
 * 
 */
package xls2history;

import java.util.Comparator;

/**
 * Compare Entry objects for fixedin field
 * 
 * @author baniu
 */
public class EntryComparator implements Comparator<Entry> {
	private int compareTo;
	
	/**
	 * Create EntryComparator object
	 * @param compareTo Code of field from Entry class to compare to
	 */
	public EntryComparator(int compareTo) {
		this.compareTo = compareTo;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Entry arg0, Entry arg1) {
		switch(compareTo) {
			case Entry.FIXEDIN:
				return arg0.fixedin.compareTo(arg1.fixedin);
			case Entry.COMPONENT:
				return arg0.component.compareTo(arg1.component);
			default:
				throw new IllegalArgumentException("Field not supported");
		}
		
	}

}
