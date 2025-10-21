#include <iostream>
using namespace std;
void helper(){
    cout<<"Keyboard"<<endl;
}
void mouse(){
    cout<<"Mouse"<<endl;
}
int myfavnumber(int n){
    return n+1;
}
int main(){
    helper();
    mouse();
    cout<<myfavnumber(9)<<endl;
}
    
