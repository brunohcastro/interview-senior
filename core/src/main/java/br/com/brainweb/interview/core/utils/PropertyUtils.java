package br.com.brainweb.interview.core.utils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class PropertyUtils {

   public static String[] getNullPropertyNames(Object source, String... extraProps) {

      final BeanWrapper src = new BeanWrapperImpl(source);

      PropertyDescriptor[] pds = src.getPropertyDescriptors();

      Set<String> emptyNames = new HashSet<>(Arrays.asList(extraProps));

      for (PropertyDescriptor pd : pds) {
         Object srcValue = src.getPropertyValue(pd.getName());

         if (srcValue == null) emptyNames.add(pd.getName());
      }

      String[] result = new String[emptyNames.size()];

      return emptyNames.toArray(result);
   }

}
