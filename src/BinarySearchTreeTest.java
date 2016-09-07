
    class BinarySearchTreeTest
    {
        static void Main(string[] args)
        {
            BinarySearchTree<int> Bst = new BinarySearchTree<int>();
            List<int> DataSet = new List<int>();
            Random Ran = new Random();
            Console.Write("Please enter the size of the data set: ");
            string DataSetSize = Console.ReadLine();
            int Size = int.Parse(DataSetSize);
            int N = Size*2; // should ensure some duplicate numbers generated on most runs
            Console.WriteLine("The size of the dataset for this run is {0}", Size);
            int Count = 0;
            while (Bst.Size < Size)
            {
                int Candidate = Ran.Next(N)*2+1;  // add odd values only
                Count++;
                if (Bst.Add(Candidate))
                {
                    DataSet.Add(Candidate);
                }
            }
            Console.WriteLine("Added {0} elements after generating {1} random numbers.",
                Size, Count);
            int ErrorCount = 0;
            Console.WriteLine("Testing the tree contents using Contains() method:");
            foreach (int d in DataSet)
            {
                if (!Bst.Contains(d))
                {
                    ErrorCount++;
                    Console.WriteLine("Error {0}: Tree does not contain value {1} from data set.",
                        ErrorCount, d);
                }
                if (Bst.Contains((d/2)*2)) // test for absent values using an even number in the range
                {
                    ErrorCount++;
                    Console.WriteLine("Error {0}: Tree contains value {1} not in data set.",
                        ErrorCount, (d/2)*2);
                }
            }
            Console.WriteLine("Testing contents complete with {0} errors ", ErrorCount);
            Console.WriteLine();
            Console.WriteLine("Testing enumerator throws an exception if tree is modified while in use.");
            try{
                foreach (int i in Bst)
                {
                    Bst.Add(-1);
                }
                ErrorCount++;
            }
            catch (InvalidOperationException) {}
            Console.WriteLine("Testing exception throwing complete. Errors now {0}.", ErrorCount);
            Console.WriteLine();
            Bst.Remove(-1);
            if (Bst.Size <= 25)
            {
                int Counter = 1;
                Console.WriteLine("Tree contents are:");
                foreach (int i in Bst)
                {
                    Console.WriteLine("Item number {0} is: {1}", Counter++, i);
                }
            }
            Console.WriteLine("Removing tree contents:");
            Count = Bst.Size;
            ErrorCount = 0;
            foreach (int d in DataSet)
            {
                if (Bst.Remove(d))
                {
                    Count--;
                    if (Count != Bst.Size)
                    {
                        ErrorCount++;  // tree size not updated
                    }
                }
                else  // d not removed
                {
                    ErrorCount++;
                }
            }
            Console.WriteLine("{0} items removed with {1} errors.", Size, ErrorCount);
            Console.WriteLine("Tree size is {0} and any remaining contents are listed below:",
                Bst.Size);
            foreach (int i in Bst)
            {
                Console.WriteLine(i);
            }
            Console.Write("Testing complete, press <Enter> to close this console window....");
            Console.ReadLine();
        }
    }
}
