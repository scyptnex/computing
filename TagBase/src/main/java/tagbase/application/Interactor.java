package tagbase.application;

import tagbase.data.Record;

public interface Interactor {

    void synchronize();

    void use(Record r);
}
