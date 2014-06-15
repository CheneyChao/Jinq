package ch.epfl.labos.iu.orm;

public class Tuple4<A, B, C, D>
{
   final A one;
   final B two;
   final C three;
   final D four;
   
   public A getOne() { return one; }
   public B getTwo() { return two; }
   public C getThree() { return three; }
   public D getFour() { return four; }
   
   public Tuple4(A one, B two, C three, D four)
   {
      this.one = one;
      this.two = two;
      this.three = three;
      this.four = four;
   }
   
   public boolean equals(Object obj)
   {
      if ( this == obj) return true;
      
      if (! (obj instanceof Tuple4)) return false;
      
      Tuple4 tuple = (Tuple4)obj;
      
      return this.one.equals(tuple.one)
         && this.two.equals(tuple.two)
         && this.three.equals(tuple.three)
         && this.four.equals(tuple.four);
   }
   
   public int hashCode()
   {
      return one.hashCode() + two.hashCode() + three.hashCode() + four.hashCode();
   }
   
}
