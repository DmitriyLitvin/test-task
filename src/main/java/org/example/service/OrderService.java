package org.example.service;

import org.example.dto.OrderRequestDto;
import org.example.entity.Order;
import org.example.mapper.OrderMapper;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final EntityManager em;

    @Autowired
    public OrderService(OrderRepository orderRepository, EntityManager em) {
        this.orderRepository = orderRepository;
        this.em = em;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }


    public void create(Order order) {
        orderRepository.save(order);
    }

    public void update(OrderRequestDto orderRequestDto, Integer id) {
        Order storedOrder = findById(id);
        if (storedOrder != null) {
            Order order =  OrderMapper.mapToOrder(orderRequestDto);
            order.setId(id);
            orderRepository.save(order);
        }
    }

    public boolean delete(Integer id) {
        Order order = orderRepository.findOne(id);
        if (order != null) {
            orderRepository.delete(id);

            return !orderRepository.exists(id);
        }

        return false;
    }

    public Page<Order> findOrderByUserNameCriteria(String userName, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> orderRoot = cq.from(Order.class);
        List<Predicate> predicates = new LinkedList<>();
        predicates.add(cb.equal(orderRoot.get("userName"), userName));

        cq.where(cb.and(predicates.toArray( new Predicate[predicates.size()])));

        List<Order> orders = em.createQuery(cq).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Order> orderRootCount = countQuery.from(Order.class);
        countQuery.select(cb.count(orderRootCount)).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Long count = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(orders, pageable, count);
    }

    @Transactional
    public Page<Order> findOrderByUserNameSpecification(String userName, Pageable pageable) {
        return orderRepository.findAll(hasUserName(userName), pageable);
    }

    private static Specification<Order> hasUserName(String userName) {
        return (order, cq, cb) -> cb.equal(order.get("userName"), userName);
    }

    public Order findById(Integer id) {
        return orderRepository.findOne(id);
    }
}
