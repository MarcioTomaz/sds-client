package com.dsClient.demo.services;

import com.dsClient.demo.DTO.ClientDTO;
import com.dsClient.demo.entities.Client;
import com.dsClient.demo.repositories.ClientRepository;
import com.dsClient.demo.services.exceptions.DataBaseException;
import com.dsClient.demo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){

        Page<Client> list = repository.findAll(pageRequest);

        return list.map(x -> new ClientDTO(x));
    }

    @Transactional
    public ClientDTO findById(Long id){
        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entity not found! "));

        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO){

        Client entity = new Client();
        copyDtoToEntity(clientDTO, entity);

        entity = repository.save(entity);

        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO){
        try {
            Client entity = repository.getOne(id);
            copyDtoToEntity(clientDTO, entity);
            entity = repository.save(entity);

            return new ClientDTO((entity));

        }catch (EntityNotFoundException e){

            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id){
        try {

            repository.deleteById(id);

        }catch (EmptyResultDataAccessException e){

            throw new DataBaseException("Integrity violation");
        }
    }



    private void copyDtoToEntity(ClientDTO clientDTO, Client entity){
        entity.setName(clientDTO.getName());
        entity.setCpf(clientDTO.getCpf());
        entity.setIncome(clientDTO.getIncome());
        entity.setBirthDate(clientDTO.getBirthDate());
        entity.setChildren(clientDTO.getChildren());
    }


}
