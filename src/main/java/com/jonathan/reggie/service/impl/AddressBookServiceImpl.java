package com.jonathan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jonathan.reggie.entity.AddressBook;
import com.jonathan.reggie.mapper.AddressBookMapper;
import com.jonathan.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
