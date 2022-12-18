package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableValidator validator;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableValidator validator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.validator = validator;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableDao.save(OrderTableRequest.to(orderTableRequest));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        List<Order> orders = orderDao.findAllByOrderTable(savedOrderTable);
        validator.validateChangeEmpty(savedOrderTable, orders);

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        validator.validateChangeNumberOfGuests(savedOrderTable, numberOfGuests);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return savedOrderTable;
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
