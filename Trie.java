package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Fawaz Tahir
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	private static String findPrefix(String word1,String word2) {
		String woah;
		String prefix = new String();
		if(word1.length()>word2.length()) {
			woah = word2;
		} else {
			woah = word1;
		}
		for(int i =0; i<woah.length(); i++) {
			if (word1.charAt(i) == word2.charAt(i)) {
				prefix = word1.substring(0,i+1);
			} else {
				break;
			}
		}
		return prefix;
	}
	private static Boolean hasPrefix(String word1, String word2) {
		if (Trie.findPrefix(word1, word2).equals("")) {
			return false;
		} else {
			return true;
		}
	}
	private static TrieNode add(String word1, TrieNode root, String[] allWords, int i, short prevIndex) {
		boolean common = false;
		TrieNode next = root;
		TrieNode ptr = root;
		short endIndex = ptr.substr.endIndex;
		short startIndex = ptr.substr.startIndex;
		int wordIndex = ptr.substr.wordIndex;
		if (Trie.hasPrefix(word1.substring(startIndex), allWords[root.substr.wordIndex].substring(startIndex))) {
			common = true;
		} else {
			if (ptr.sibling!= null) {
				next = next.sibling;
				ptr.sibling = Trie.add(word1, next, allWords, i, prevIndex);
				return root;
			}
		}
		String prefix = Trie.findPrefix(word1, allWords[ptr.substr.wordIndex].substring(startIndex,endIndex+1));
		
		if (common == true) {
			if (ptr.firstChild != null && prefix.length()-1 == ptr.substr.endIndex) {
				if (allWords[ptr.substr.wordIndex].substring(startIndex, endIndex+1).equals(prefix) ) {
					next = next.firstChild;
				}
					if(!Trie.hasPrefix(allWords[next.substr.wordIndex].substring(endIndex+1), word1.substring(endIndex+1))&& next.firstChild!=null &&!Trie.hasPrefix(allWords[next.sibling.substr.wordIndex].substring(endIndex+1), word1.substring(endIndex+1))) {
						while (next.sibling != null) {
							next = next.sibling;
						}
						next.sibling = new TrieNode(new Indexes(i,(short)(endIndex+1),(short)(allWords[i].length()-1)),null,null);
					} else {
						next = ptr;
				next = next.firstChild;
				prevIndex = ptr.substr.endIndex;
				ptr.firstChild = Trie.add(word1, next, allWords, i, prevIndex);
	//FIXING THE START INDEX
				while (ptr.firstChild != null) {
					ptr = ptr.firstChild;
					if (ptr.firstChild != null ) {
						ptr.substr.startIndex = (short)(prevIndex+1);
						if (ptr.firstChild.substr.endIndex == allWords[ptr.firstChild.substr.wordIndex].length()-1 && ptr.sibling != null) {
							while (ptr.sibling != null) {
								ptr = ptr.sibling;
								if (ptr.sibling != null) {
									if (ptr.substr.endIndex != allWords[ptr.substr.wordIndex].length()-1) {
										ptr.substr.startIndex = (short)(prevIndex+1);
									}
								} else {
									if (ptr.firstChild == null) {
										break;
									} else {
										ptr.substr.startIndex = (short)(prevIndex+1);
									}
								}
							}
						}
					} else {
						while (ptr.sibling != null) {
							ptr = ptr.sibling;
							if (ptr.sibling != null) {
								if (ptr.substr.endIndex != allWords[ptr.substr.wordIndex].length()-1) {
									ptr.substr.startIndex = (short)(prevIndex+1);
								}
							} else {
								if (ptr.firstChild == null) {
									break;
								} else {
									ptr.substr.startIndex = (short)(prevIndex+1);
								}
							}
						}
					}
					
				}
				}
			} else if (ptr.sibling != null){
				if (Trie.hasPrefix(word1.substring(startIndex), allWords[next.substr.wordIndex].substring(startIndex))) {
					next = Trie.add(word1, new TrieNode(new Indexes(next.substr.wordIndex,next.substr.startIndex,next.substr.endIndex),null,null), allWords, i, prevIndex);
					next.sibling = ptr.sibling;
					root = next;
				} else {
					next = next.sibling;
					ptr = Trie.add(word1, next, allWords, i, prevIndex);
				}
			} else if (prefix.length()-1 != ptr.substr.endIndex && ptr.firstChild != null) {
				root = new TrieNode(new Indexes(ptr.substr.wordIndex,ptr.substr.startIndex,(short)(prefix.length()-1)),ptr,null);
				root.firstChild.sibling = new TrieNode(new Indexes(i,(short)(root.substr.endIndex+1),(short)(allWords[i].length()-1)),null,null); 
			} else {
				prefix = Trie.findPrefix(word1,allWords[next.substr.wordIndex]);
				next.firstChild = new TrieNode(new Indexes(next.substr.wordIndex,(short)(prefix.length()),(short)(allWords[next.substr.wordIndex].length()-1)),null,null);
				next.firstChild.sibling = new TrieNode (new Indexes(i,(short)(prefix.length()),(short)(allWords[i].length()-1)),null,null);
				next.substr.startIndex = 0;
				next.substr.endIndex = (short)(prefix.length()-1);
			}
		} else {
			prefix = Trie.findPrefix(word1, allWords[ptr.substr.wordIndex]);
			next.sibling = new TrieNode(new Indexes (i,(short)prefix.length(),(short)(allWords[i].length()-1)),null,null);
		}
		return root;
	}
	
	
	
	
	
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null,null,null);
		if (allWords.length == 0) {
			return root;
		}
		if (root.firstChild == null) {
			root.firstChild = new TrieNode(new Indexes(0,(short)0,(short)(allWords[0].length()-1)),null,null); 
		}
		TrieNode ptr = root.firstChild;
		for (int i = 1; i<allWords.length; i++) {
			ptr = root.firstChild;
			root.firstChild = Trie.add(allWords[i], ptr, allWords, i, (short)0);			
		}
		
	return root;
	}
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		if(root == null){
			return null;
		}
		ArrayList<TrieNode> winner = new ArrayList<>();
		TrieNode ptr = root;
		while(ptr != null){
			if(ptr.substr == null){ 
				ptr = ptr.firstChild;
				}
			String all = allWords[ptr.substr.wordIndex];
			String exam = all.substring(0, ptr.substr.endIndex+1);
			if(all.startsWith(prefix) || prefix.startsWith(exam)){
				if(ptr.firstChild != null){ 
					winner.addAll(completionList(ptr.firstChild,allWords,prefix));
					ptr = ptr.sibling;
				}
				else{ 
					winner.add(ptr);
					ptr = ptr.sibling;
				}
			}
			else{
				ptr = ptr.sibling;
			}
		}
		return winner;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
