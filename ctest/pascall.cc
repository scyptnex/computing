#include <iostream>
#include <vector>

using namespace std;

void pascallise(vector<int>* above, vector<int>* layer){
	if(above == NULL) layer->push_back(1);
	else{
		for(int i=0; i<=above->size(); i++){
			int al = 0;
			int ar = 0;
			if(i > 0) al = (*above)[i-1];
			if(i < above->size()) ar = (*above)[i];
			layer->push_back(al+ar);
		}
	}
}

int main(int argc, char** argv){
	int levels = 0;
	vector< vector<int> > pct;
	cout << "How many levels?" << endl;
	cin >> levels;
	if(levels == 0){
		cerr << "Error!" << endl;
		return 0;
	}
	cout << "Pascal's triangle to " << levels << " choose " << levels << endl;
	for(int i=0; i<levels; i++){
		vector<int> addit;
		vector<int>* above = NULL;
		if(i != 0) above = &pct[i-1];
		pascallise(above, &addit);
		pct.push_back(addit);		
	}
	for(vector<vector<int> >::iterator lit=pct.begin(); lit < pct.end(); lit++){
		for(vector<int>::iterator vit = lit->begin(); vit < lit->end(); vit++){
			cout << *vit << "\t";
		}
		cout << endl;
	}
	return 0;
}
