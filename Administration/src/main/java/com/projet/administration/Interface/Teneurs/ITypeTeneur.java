package com.projet.administration.Interface.Teneurs;

import com.projet.administration.Entity.Teneur.TypeTeneur;
import java.util.List;

public interface ITypeTeneur {

    public TypeTeneur addTypeTeneur(TypeTeneur typeTeneur) ;
    public TypeTeneur updateTypeTeneurWithId(Long id, TypeTeneur typeTeneur);
    public List<TypeTeneur> getAllTypeTeneur();
    public String deleteTypeTeneur(Long id);
}
