import java.util.*;
import java.io.IOException;
import java.time.*;
class income extends Thread{
    Scanner sc=new Scanner(System.in);
    double inc;
    void getincome(){
       System.out.println("Enter income=");
       inc=sc.nextDouble();
       try {
        Thread.sleep(2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }
 }
class savings extends income{
    double sv;
}
class expenses extends savings{
    double food;
    double rent;
    double entertainment;
    double sumd;
    String yearInput,monthInput,dayInput;
    Scanner sc=new Scanner(System.in);
    final double inctax=100;
    double inhand;
    final void incometax(){
        inhand=inc-inctax;
        System.out.println("Income tax="+inctax);
    }
    void totalspending(){
      sumd=food+rent+entertainment;
      System.out.println("Total spending for the day="+sumd);
    }
    void viewexpenses(){
        System.out.println("Spending of the Day:-");
        System.out.println("Food="+food);
        System.out.println("Rent="+rent);
        System.out.println("Entertainment="+entertainment);
        totalspending();
        incometax();
    }
    void getsavings(){
        sv=inhand-sumd;
        System.out.println("Savings="+sv);
    }
    void get_expenses(){
       System.out.println("Enter food expense=");
       food=sc.nextDouble();
       System.out.println("Enter rent=");
       rent=sc.nextDouble();
       System.out.println("Enter chil cost=");
       entertainment=sc.nextDouble();
    }
}
class helper extends Thread{
    public void run(){
        for(int i=1;i<6;i++){
        System.out.println("Done!");
        try {
            sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.yield();
    }
  }
}
class retirement_savings extends savings{
    double annual_withdrawal, return_rate, retirement_years;
    double calculat_retirement_savings(double annual_withdrawal,double return_rate,double retirement_years){
            double annual_returns=sv+(sv*return_rate);
            return (annual_returns-annual_withdrawal)*retirement_years;
    }
}
public class Math {
    public static void main(String[] args) throws IOException {
        expenses e=new expenses();
        System.out.println("Enter date input=");
        try (Scanner sc=new Scanner(System.in);) {
            e.dayInput=sc.nextLine();
            e.monthInput=sc.nextLine();
            e.yearInput=sc.nextLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
       
        helper h=new helper();
        h.setPriority(Thread.MAX_PRIORITY);
        h.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        int y = Integer.parseInt( e.yearInput );
        int m = Integer.parseInt( e.monthInput );  // 1-12 for January-December.
        int d = Integer.parseInt( e.dayInput );
        LocalDate ld = LocalDate.of( y , m , d );
        System.err.println("The date:"+ld);
        e.getincome();
        e.get_expenses();
        e.viewexpenses();
        e.getsavings();
        
        
    }
}