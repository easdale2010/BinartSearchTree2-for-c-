

class TreeSet
{
    /// <summary>
    /// A class that represents a binary search tree. This class was originally written in
    /// Java by William Collins to accompany the textbook, 'Data Structures and the Java 
    /// Collections Framework' published by John Wiley and Sons. Translated into C# by Richard Beeby.
    /// </summary>
    public class BinarySearchTree<E> : IEnumerable<E> where E : IComparable<E>
    {

        // The root of this tree. This is null when the tree is empty.
        Entry Root;

        // The number of entries in this tree. This is zero when the tree is empty.
        int TreeSize;
     
        // Counts the number of modifications made to this tree.
        int ModCount = 0;
  
         /// <summary>
         /// Initializes this BinarySearchTree object to be empty, to contain only elements
         /// of type E, to be ordered by the IComparable interface, and to contain no 
         /// duplicate elements.
         /// </summary>
        public BinarySearchTree() 
        {
            Root = null;
            TreeSize = 0;  
        } // default constructor
        
        ///<summary>
        /// Initializes this BinarySearchTree object to contain a shallow Copy of
        /// a specified BinarySearchTree object.
        /// The worstTime(n) is O(n), where n is the number of elements in the
        /// specified BinarySearchTree object.
        ///</summary>
        ///<param name="otherTree">The specified BinarySearchTree object that this
        /// BinarySearchTree object will be assigned a shallow copy of.
        ///</param>
        public BinarySearchTree (BinarySearchTree<E> otherTree)
        {
             Root = Copy (otherTree.Root, null);
             TreeSize = otherTree.TreeSize;  
        } // Copy constructor


        protected Entry Copy (Entry p, Entry parent)
        {
            if (p != null)
            {
                Entry q = new Entry (p.Element, parent);
                q.Left = Copy (p.Left, q);
                q.Right = Copy (p.Right, q);
                return q;
            } // if
            return null;
        } // method Copy
        
    
         ///<value>The number of elements in this BinarySearchTree object.</value>
        public int Size
        {
            get
            {
                return TreeSize;
            }
        }
  

        /**
         *  <summary>Returns an enumerator positioned at the smallest element in this 
         *  BinarySearchTree object.</summary>
         *  <returns>An enumerator positioned at the smallest element in this
         *  BinarySearchTree object.</returns>
         */
        public IEnumerator<E> GetEnumerator()
        {
             return new TreeIterator(this);
        } // method iterator

        
        IEnumerator IEnumerable.GetEnumerator()
        {
            throw new NotImplementedException();
        }
         

        /**
         * <summary> 
         * Determines if there is at least one element in this BinarySearchTree object that
         *  equals a specified element.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *  </summary>
         *  <param name="obj">The element sought in this BinarySearchTree object.</param>
         *  <returns> true - if there is an element in this BinarySearchTree object that
         *  equals obj; otherwise, return false.<returns>
         *  <exception cref="NullReferenceException">If obj is null.</exception>
         */ 
        public bool Contains (E obj) 
        {
            return GetEntry (obj) != null;
        } // method contains


        /**
         * <summary> 
         * Ensures that this BinarySearchTree object contains a specified element.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *  </summary>
         *
         *  <param name="element">The element whose presence is ensured in this 
         *  BinarySearchTree object.</param>
         *
         *  <returns> true - if this BinarySearchTree object changed as a result of this
         *  method call (that is, if element was actually inserted); otherwise, return false.</returns>
         *
         *  <exception cref="NullReferenceException">If element is null.</exception>
         */
        public bool Add (E element)  
        {
            if (Root == null) 
            {
                if (element == null)
                    throw new NullReferenceException();
                Root = new Entry (element, null);
                TreeSize++;
                ModCount++;
                return true;
            } // empty tree
            else 
            {
                Entry temp = Root;

                int comp;

                while (true) 
                {
                    comp =  element.CompareTo (temp.Element);
                    if (comp == 0)
                        return false;
                    if (comp < 0)
                        if (temp.Left != null)
                            temp = temp.Left;
                        else 
                        {
                            temp.Left = new Entry (element, temp);
                            TreeSize++;
                            ModCount++;
                            return true;
                        } // temp.Left == null
                     else if (temp.Right != null)
                         temp = temp.Right;
                     else 
                     {
                         temp.Right = new Entry (element, temp);
                         TreeSize++;
                         ModCount++;
                         return true;
                     } // temp.Right == null
                } // while
            } // root not null
        } // method Add


        /**
         *  <summary> Ensures that this BinarySearchTree object does not contain a specified 
         *  element.
         *  The worstTime(n) is O(n) and averageTime(n) is O(log n).
         *  </summary>
         *  <param name="obj"> the object whose absence is ensured in this BinarySearchTree object.</param>
         *  <returns> true - if this BinarySearchTree object changed as a result of this
         *                method call (that is, if obj was actually Removed); otherwise,
         *                return false.</returns>
         *  <exception cref="NullReferenceException"> - if obj is null.</exception>
         */
        public bool Remove (E obj)
        {
            Entry e = GetEntry (obj);
            if (e == null)
                return false;
            DeleteEntry (e);
            ModCount++;
            return true;
        } // method Remove

        /**
         *  <summary>Finds the Entry object that houses a specified element, if there is such an Entry.
         *  The worstTime(n) is O(n), and averageTime(n) is O(log n).</summary>
         *  <param name="obj">The element whose Entry is sought.</param>
         *  <returns>The Entry object that houses obj - if there is such an Entry;
         *  otherwise, return null.</returns>
         *  <exception cref="NullReferenceException">If obj is null.</exception>
         */
        protected Entry GetEntry (E obj) 
        {
            int comp;

            if (obj == null)
               throw new NullReferenceException();
            Entry e = Root;
            while (e != null) 
            {
                comp = obj.CompareTo (e.Element);
                if (comp == 0)
                    return e;
                else if (comp < 0)
                    e = e.Left;
                else
                    e = e.Right;
            } // while
            return null;
        } // method GetEntry
    
         /**
          <summary>
          Deletes the element in a specified Entry object from this BinarySearchTree.
          </summary>
          <param name="p">The Entry object whose element is to be deleted from this
          BinarySearchTree object.</param>
          <returns>The Entry object that was actually deleted from this BinarySearchTree
          object.</returns>
          */
        protected Entry DeleteEntry (Entry p) 
        {
            TreeSize--;

            // If p has two children, replace p's element with p's successor's
            // element, then make p reference that successor.
            if (p.Left != null && p.Right != null) 
            {
                Entry s = successor (p);
                p.Element = s.Element;
                p = s;
            } // p had two children


            // At this point, p has either no children or one child.

            Entry replacement;
         
            if (p.Left != null)
                replacement = p.Left;
            else
                replacement = p.Right;

            // If p has at least one child, link replacement to p.Parent.
            if (replacement != null) 
            {
                replacement.Parent = p.Parent;
                if (p.Parent == null)
                    Root = replacement;
                else if (p == p.Parent.Left)
                    p.Parent.Left  = replacement;
                else
                    p.Parent.Right = replacement;
            } // p has at least one child  
            else if (p.Parent == null)
                Root = null;
            else 
            {
                if (p == p.Parent.Left)
                    p.Parent.Left = null;
                else
                    p.Parent.Right = null;        
            } // p has a parent but no children
            return p;
        } // method DeleteEntry


         ///<summary>Finds the successor of a specified Entry object in this BinarySearchTree.
         ///The worstTime(n) is O(n) and averageTime(n) is constant.</summary>
         ///<param name="e">The Entry object whose successor is to be found.</param>
         ///<returns>The successor of e, if e has a successor; otherwise, return null.</returns>
        protected Entry successor (Entry e) 
        {
            if (e == null)
                return null;
            else if (e.Right != null) 
            {
                // successor is leftmost Entry in right subtree of e
                Entry p = e.Right;
                while (p.Left != null)
                    p = p.Left;
                return p;

            } // e has a right child
            else 
            {
                // go up the tree to the left as far as possible, then go up
                // to the right.
                Entry p = e.Parent;
                Entry ch = e;
                while (p != null && ch == p.Right) 
                {
                    ch = p;
                    p = p.Parent;
                } // while
                return p;
            } // e has no right child
        } // method successor
    
        protected class TreeIterator : IEnumerator<E>
        {
            BinarySearchTree<E> MyTree;

            Entry LastReturned = null,
                  Next;

            E current;

            int ModCountOnCreation;

            /**
             <summary>
             Positions this TreeIterator to the smallest element, according to the IComparable
             interface, in the BinarySearchTree object.
             The worstTime(n) is O(n) and averageTime(n) is O(log n).
             </summary>
             */
            public TreeIterator(BinarySearchTree<E> tree) 
            {
                MyTree = tree;
                ModCountOnCreation = tree.ModCount;
            } // default constructor

            public E Current {
                get
                {
                    if (LastReturned == null)
                    {
                        throw new InvalidOperationException();
                    }
                    return current;
                }
                private set
                {
                    current = value;
                }
            }

            object IEnumerator.Current
            {
                get
                {
                    throw new NotImplementedException();
                }
            }

            public void Dispose()
            {
                Next = null;
                LastReturned = null;
                MyTree = null;
            }

            public bool MoveNext() 
            {
                if (ModCountOnCreation != MyTree.ModCount)
                {
                    throw new InvalidOperationException();
                }
                if (LastReturned == null)
                {
                    Next = MyTree.Root;
                    if (Next != null)
                        while (Next.Left != null)
                            Next = Next.Left;
                }
                if (Next == null)
                    return false;
                Current = Next.Element;
                LastReturned = Next;
                Next = MyTree.successor (LastReturned);          
                return true;
            } // method next

             ///<summary>Resets the enumerator to the smallest element in the tree</summary>
            public void Reset() 
            {
                if (ModCountOnCreation != MyTree.ModCount)
                {
                    throw new InvalidOperationException();
                }
                Next = null;
                LastReturned = null;
            } // method Reset     

        } // class TreeIterator

        protected class Entry
        {
            public E Element { get; set; }

            protected Entry left = null,
                               right = null;

            public Entry Left
            {
                get
                {
                    return left;
                }
                set
                {
                    left = value;
                }
            }

            public Entry Right
            {
                get
                {
                    return right;
                }
                set
                {
                    right = value;
                }
            }


            public Entry Parent { get; set; }

            /**
             <summary>Initializes this Entry object.</summary>
             <remarks>This default constructor is defined for the sake of subclasses of
             the BinarySearchTree class.</remarks>
             */
            public Entry() { }



             /// <summary>
             /// Initializes this Entry object from element and parent.
             /// </summary>
             /// <param name="element">The element value for this entry</param>
             /// <param name="parent">The link to the parent entry of this entry</param>
             public Entry (E element, Entry parent) 
             {
                 Element = element;
                 Parent = parent;
             } // constructor

        } // class Entry

    }
}
