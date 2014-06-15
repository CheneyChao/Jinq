package ch.epfl.labos.iu.orm;

public class Tuple8<A, B, C, D, E, F, G, H>
{
   final A one;
   final B two;
   final C three;
   final D four;
   final E five;
   final F six;
   final G seven;
   final H eight;
   
   public A getOne() { return one; }
   public B getTwo() { return two; }
   public C getThree() { return three; }
   public D getFour() { return four; }
   public E getFive() { return five; }
   public F getSix() { return six; }
   public G getSeven() { return seven; }
   public H getEight() { return eight; }
   
   public Tuple8(A one, B two, C three, D four, E five, F six, G seven, H eight)
   {
      this.one = one;
      this.two = two;
      this.three = three;
      this.four = four;
      this.five = five;
      this.six = six;
      this.seven = seven;
      this.eight = eight;
   }
   
   public boolean equals(Object obj)
   {
      if ( this == obj) return true;
      
      if (! (obj instanceof Tuple8)) return false;
      
      Tuple8 tuple = (Tuple8)obj;
      
      return this.one.equals(tuple.one)
         && this.two.equals(tuple.two)
         && this.three.equals(tuple.three)
         && this.four.equals(tuple.four)
         && this.five.equals(tuple.five)
         && this.six.equals(tuple.six)
         && this.seven.equals(tuple.seven)
         && this.eight.equals(tuple.eight);
   }
   
   public int hashCode()
   {
      return one.hashCode() + two.hashCode() + three.hashCode() + four.hashCode()
         + five.hashCode() + six.hashCode() + seven.hashCode() + eight.hashCode();
   }
   
}
