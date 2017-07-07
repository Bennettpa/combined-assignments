package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import com.cooksys.ftd.assignments.collections.model.WageSlave;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

	private HashSet<Capitalist> corp = new HashSet<Capitalist>();

	/**
	 * Adds a given element to the hierarchy.
	 * <p>
	 * If the given element is already present in the hierarchy, do not add it
	 * and return false
	 * <p>
	 * If the given element has a parent and the parent is not part of the
	 * hierarchy, add the parent and then add the given element
	 * <p>
	 * If the given element has no parent but is a Parent itself, add it to the
	 * hierarchy
	 * <p>
	 * If the given element has no parent and is not a Parent itself, do not add
	 * it and return false
	 *
	 * @param capitalist
	 *            the element to add to the hierarchy
	 * @return true if the element was added successfully, false otherwise
	 */
	@Override
	public boolean add(Capitalist capitalist) {
		// if capitalist is null or is in the hierarchy return false
		if (capitalist == null || this.has(capitalist))
			return false;
		// if capitalist has a parent
		if (capitalist.hasParent()) {
			// if the parent is not in the hierarchy add the parent then the
			// capitalist and return true
			if (!this.has(capitalist.getParent()))
				this.add(capitalist.getParent());
			corp.add(capitalist);
			return true;
		}
		// if capitalist is not a parent and is a child return false
		if (capitalist instanceof FatCat) {
			corp.add(capitalist);
			return true;
		}
		return false;
	}

	/**
	 * @param capitalist
	 *            the element to search for
	 * @return true if the element has been added to the hierarchy, false
	 *         otherwise
	 */
	@Override
	public boolean has(Capitalist capitalist) {
		return corp.contains(capitalist);
	}

	/**
	 * @return all elements in the hierarchy, or an empty set if no elements
	 *         have been added to the hierarchy
	 */
	@Override
	public Set<Capitalist> getElements() {
		return new HashSet<Capitalist>(corp);
	}

	/**
	 * @return all parent elements in the hierarchy, or an empty set if no
	 *         parents have been added to the hierarchy
	 */
	@Override
	public Set<FatCat> getParents() {
		HashSet<FatCat> parents = new HashSet<FatCat>();
		corp.forEach(capitalist -> {
			if (capitalist instanceof FatCat) {
				parents.add((FatCat) capitalist);
			}
		});
		return parents;
	}

	/**
	 * @param fatCat
	 *            the parent whose children need to be returned
	 * @return all elements in the hierarchy that have the given parent as a
	 *         direct parent, or an empty set if the parent is not present in
	 *         the hierarchy or if there are no children for the given parent
	 */
	@Override
	public Set<Capitalist> getChildren(FatCat fatCat) {
		HashSet<Capitalist> children = new HashSet<Capitalist>();
		// if fatCat is null return empty set
		if (fatCat == null || !(this.has(fatCat)))
			return children;
		// Check every capitalist in hierarchy and see if the parent is equal
		// fatCat if so add it to children
		corp.forEach(capitalist -> {
			if (capitalist.hasParent() && capitalist.getParent().equals(fatCat)) {
				children.add(capitalist);
			}
		});
		return children;
	}

	/**
	 * @return a map in which the keys represent the parent elements in the
	 *         hierarchy, and the each value is a set of the direct children of
	 *         the associate parent, or an empty map if the hierarchy is empty.
	 */
	@Override
	public Map<FatCat, Set<Capitalist>> getHierarchy() {
		HashMap<FatCat, Set<Capitalist>> hierarchy = new HashMap<>();
		corp.forEach(capitalist -> {
			if (capitalist instanceof FatCat && !(hierarchy.containsKey((FatCat) capitalist))) {
				hierarchy.put((FatCat) capitalist, this.getChildren((FatCat) capitalist));
			}
		});
		return hierarchy;
	}

	/**
	 * @param capitalist
	 * @return the parent chain of the given element, starting with its direct
	 *         parent, then its parent's parent, etc, or an empty list if the
	 *         given element has no parent or if its parent is not in the
	 *         hierarchy
	 */
	@Override
	public List<FatCat> getParentChain(Capitalist capitalist) {
		ArrayList<FatCat> list = new ArrayList<FatCat>();
		if (capitalist == null || !this.has(capitalist))
			return list;
		Capitalist c = capitalist;
		while (c.hasParent()) {
			list.add(c.getParent());
			c = c.getParent();
		}
		return list;
	}
}
